package part2.messaging;

import akka.actor.ActorRef;

import java.util.UUID;

public class DBSaveRequest {
    public final String prodName;
    public final ActorRef respondTo;
    public final UUID requestID;

    public DBSaveRequest(String prodName, ActorRef respondTo, UUID requestID) {
        this.prodName = prodName;
        this.respondTo = respondTo;
        this.requestID = requestID;
    }
}
