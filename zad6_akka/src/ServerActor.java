import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import messaging.*;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ServerActor extends AbstractActor {
    private final ActorRef store1;
    private final ActorRef store2;
    private final Map<UUID, ServerRequestStoreObj> requestMap = new HashMap<>();

    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    ServerActor() {
        store1 = getContext().actorOf(Props.create(StoreActor.class), "store1");
        store2 = getContext().actorOf(Props.create(StoreActor.class), "store2");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PriceRequest.class, priceRequest -> {
                    PriceRequest serverInitedRequest = new PriceRequest(getSelf(), priceRequest.prodName);  // to change respondTo
                    requestMap.put(serverInitedRequest.id, new ServerRequestStoreObj(priceRequest.respondTo, priceRequest.prodName));

                    // dispatch requests

                    store1.tell(serverInitedRequest, getSender());
                    store2.tell(serverInitedRequest, getSender());

                    // set timeout
                    getContext().system().scheduler()
                            .scheduleOnce(
                                    Duration.ofMillis(Constants.TIMEOUT),
                                    getSelf(),
                                    new TimeoutMessage(serverInitedRequest.id),
                                    getContext().getSystem().dispatcher(),
                                    ActorRef.noSender()
                            );
                })
                .match(PriceResponse.class, priceResponse -> {
                    if (!this.requestMap.containsKey(priceResponse.requestID)) {
                        return;
                    }
                    ServerRequestStoreObj s = this.requestMap.get(priceResponse.requestID);
                    s.responseList.add(priceResponse);

                    if (s.responseList.size() == 2) {
                        PriceResponse resp1 = s.responseList.get(0);
                        PriceResponse resp2 = s.responseList.get(1);
                        PriceResponse resp = resp1.price < resp2.price ? resp1 : resp2;
                        resp.prodName = s.prodName;
                        s.respondTo.tell(resp, getSelf());

                        this.requestMap.remove(priceResponse.requestID);
                    }
                })
                .match(TimeoutMessage.class, timeoutMessage -> {
                    ServerRequestStoreObj s = this.requestMap.get(timeoutMessage.requestId);
                    if (s == null) {
                        return;
                    }

                    PriceResponse resp;
                    if (s.responseList.size() == 0) {
                        resp = new PriceResponse(timeoutMessage.requestId, 0);
                        resp.msg = "Request timed out. No price found";
                    } else {
                        if (s.responseList.size() == 1) {
                            resp = s.responseList.get(0);
                        } else if (s.responseList.size() == 2) {
                            PriceResponse resp1 = s.responseList.get(0);
                            PriceResponse resp2 = s.responseList.get(1);
                            resp = resp1.price < resp2.price ? resp1 : resp2;
                        } else {
                            log.error("Strange... Response list longer than 2. THIS SHOULDN'T HAPPEN");
                            return;
                        }
                    }

                    this.requestMap.remove(timeoutMessage.requestId);

                    resp.prodName = s.prodName;
                    s.respondTo.tell(resp, getSelf());
                })
                .matchAny(o -> log.info("Unexpected message: " + o))
                .build();
    }
}
