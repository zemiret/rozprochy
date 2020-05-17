
import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import messaging.PriceRequest;

public class StoreActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PriceRequest.class, priceReq -> {
                    // TODO: Make sure this actor dies in time (and is error messaging sth to care about?)
                    getContext().actorOf(Props.create(PriceEvaluatorActor.class))
                            .tell(priceReq, getSender());
                })
                .matchAny(o -> log.info("Unknown message: " + o))
                .build();
    }
}
