package part3;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import part3.messaging.ClientRequest;
import part3.messaging.PriceRequest;
import part3.messaging.PriceResponse;

public class ClientActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(ClientRequest.class, req -> {
                    req.server.tell(new PriceRequest(getSelf(), req.prodName), getSelf());
                })
                .match(PriceResponse.class, priceResponse -> {
                    if (priceResponse.msg.equals("")) {
                        log.info("GOT PRICE FOR " + priceResponse.prodName + ": " + priceResponse.price);
                    } else {
                        log.info(priceResponse.msg);
                    }
                })
                .matchAny(o -> log.info("Unknown message: " + o))
                .build();
    }
}
