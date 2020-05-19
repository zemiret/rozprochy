package part2;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import part2.messaging.PriceRequest;

public class StoreActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PriceRequest.class, priceReq -> {
                    getContext().actorOf(Props.create(PriceEvaluatorActor.class))
                            .tell(priceReq, getSender());
                })
                .matchAny(o -> log.info("Unknown message: " + o))
                .build();
    }
}
