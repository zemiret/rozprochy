package part1;

import akka.actor.AbstractActor;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import part1.messaging.PriceRequest;
import part1.messaging.PriceResponse;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class PriceEvaluatorActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(PriceRequest.class, priceReq -> {
                    int sleepTime = ThreadLocalRandom.current().nextInt(Constants.MIN_SLEEP, Constants.MAX_SLEEP);
                    TimeUnit.MILLISECONDS.sleep(sleepTime);
                    int price = ThreadLocalRandom.current().nextInt(Constants.MIN_PRICE, Constants.MAX_PRICE);
                    priceReq.respondTo.tell(new PriceResponse(
                                    priceReq.id,
                                    price
                            ),
                            getSelf());
                })
                .matchAny(o -> log.info("Unknown message: " + o))
                .build();
    }
}
