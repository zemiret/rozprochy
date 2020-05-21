package part3.messaging;

import akka.http.javadsl.server.Route;

import java.util.function.Function;

public class HttpPriceRequest {
    public final String prodName;
    public final Function<String, Route> complete;  // toDO: TYPE

    public HttpPriceRequest(String prodName, Function<String, Route> complete) {
        this.prodName = prodName;
        this.complete = complete;
    }
}
