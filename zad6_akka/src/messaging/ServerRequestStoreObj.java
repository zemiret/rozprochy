package messaging;

import akka.actor.ActorRef;

import java.util.LinkedList;
import java.util.List;

public class ServerRequestStoreObj {
    public final ActorRef respondTo;
    public final List<PriceResponse> responseList;
    public final String prodName;

    public ServerRequestStoreObj(ActorRef respondTo, String prodName) {
        this.respondTo = respondTo;
        this.prodName = prodName;
        this.responseList = new LinkedList<>();
    }
}
