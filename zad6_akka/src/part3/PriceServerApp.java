package part3;

import akka.NotUsed;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.http.javadsl.ConnectHttp;
import akka.http.javadsl.Http;
import akka.http.javadsl.ServerBinding;
import akka.http.javadsl.model.HttpRequest;
import akka.http.javadsl.model.HttpResponse;
import akka.http.javadsl.model.StatusCodes;
import akka.http.javadsl.server.*;
import akka.http.javadsl.server.directives.FutureDirectives;
import akka.pattern.Patterns;
import akka.stream.Materializer;
import akka.stream.javadsl.Flow;
import akka.util.Timeout;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import part3.messaging.PriceRequest;
import part3.messaging.PriceResponse;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;

import static akka.http.javadsl.server.Directives.onSuccess;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class PriceServerApp extends AllDirectives {
    public static void main(String[] args) throws Exception {
        ActorSystem system = ActorSystem.create("routes");

        final Http http = Http.get(system);
        final Materializer materializer = Materializer.createMaterializer(system);
        PriceServerApp app = new PriceServerApp(system, materializer);

        final Flow<HttpRequest, HttpResponse, NotUsed> routeFlow = app.createRoutes().flow(system, materializer);
        final CompletionStage<ServerBinding> binding = http.bindAndHandle(routeFlow,
                ConnectHttp.toHost("localhost", 8080),
                materializer);

        System.out.println("Server online at http://localhost:8080/\nPress RETURN to stop...");
        System.in.read(); // let it run until user presses return

        binding
                .thenCompose(ServerBinding::unbind) // trigger unbinding from the port
                .thenAccept(unbound -> system.terminate()); // and shutdown when done
    }

    private final ActorRef server;
    private final ActorSystem system;
    private final Materializer materializer;

    PriceServerApp(ActorSystem system, Materializer materializer) {
        this.system = system;
        this.server = system.actorOf(Props.create(ServerActor.class), "server-actor");
        this.materializer = materializer;
    }

    private Route createRoutes() {
        final ExceptionHandler loggerHandler = ExceptionHandler.newBuilder()
                .matchAny(o -> complete(StatusCodes.BAD_REQUEST, "Error: " + o))
                .build();

        final RejectionHandler defaultHandler = RejectionHandler.defaultHandler();

        return concat(
                createPriceRoute(),
                createReviewRoute()
        ).seal(defaultHandler, loggerHandler);
    }

    private Route createReviewRoute() {
        PathMatcher1<String> priceMatcher = PathMatchers
                .segment("review")
                .slash(
                        PathMatchers.segment(Pattern.compile(".*"))
                );

        return path(priceMatcher, prodName ->
                get(() -> {
                            try {
                                String url = "https://www.opineo.pl/?szukaj=" +
                                        URLEncoder.encode(prodName, StandardCharsets.UTF_8.toString()) +
                                        "&s=2";

                                CompletionStage<Object> f = Http.get(system)
                                        .singleRequest(HttpRequest.create(url))
                                        .thenCompose(response ->
                                                response.entity().toStrict(5000, materializer))
                                        .thenApply(entity ->
                                                entity.getData().utf8String())
                                        .thenApply(html -> {
                                                    Elements pls = Jsoup.parse(html).body().getElementsByClass("pls");
                                                    if (pls.first() == null) {
                                                        return "No element found";
                                                    }

                                                    Elements plsChildren = pls.first().children();
                                                    if (plsChildren.first() == null) {
                                                        return "No element found";
                                                    }

                                                    Elements plAttr = plsChildren.first()
                                                            .getElementsByClass("pl_attr");

                                                    if (plAttr.first() == null) {
                                                        return "No element found";
                                                    }

                                                    return plAttr.first()
                                                            .getElementsByTag("li")
                                                            .eachText().stream()
                                                            .filter(a -> !a.equals("..."))
                                                            .reduce("", (acc, cur) -> acc + "\n" + cur);
                                                }
                                        );

                                return onSuccess(f, review -> {
                                    String reviewStr = (String) review;
                                    return complete(reviewStr);
                                });
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }

                            return complete("Something went wrong =)");
                        }
                )
        );
    }

    private Route createPriceRoute() {
        PathMatcher1<String> priceMatcher = PathMatchers
                .segment("price")
                .slash(
                        PathMatchers.segment(Pattern.compile(".*"))
                );

        return path(priceMatcher, prodName ->
                get(() -> {
                            CompletionStage<Object> f = Patterns.askWithReplyTo(this.server, ref -> new PriceRequest(ref, prodName), Duration.ofSeconds(2));

                            return onSuccess(f, (res) -> {
                                PriceResponse resp = (PriceResponse) res;
                                if (resp.msg.equals("")) {
                                    return complete("PRICE FOR " + resp.prodName + ": " + resp.price);
                                } else {
                                    return complete(resp.msg);
                                }
                            });
                        }
                )
        );
    }
}