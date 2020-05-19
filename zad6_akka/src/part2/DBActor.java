package part2;

import akka.actor.AbstractActor;
import akka.actor.Props;
import akka.event.Logging;
import akka.event.LoggingAdapter;
import part2.messaging.DBSaveRequest;

public class DBActor extends AbstractActor {
    private final LoggingAdapter log = Logging.getLogger(getContext().getSystem(), this);

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(DBSaveRequest.class, dbSaveRequest -> {
                    getContext().actorOf(Props.create(DBRequestHandler.class)).tell(dbSaveRequest, getSender());
                })
                .matchAny(o -> log.info("Unexpected message: " + o))
                .build();
    }
}
