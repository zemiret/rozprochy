package part3.messaging;

import akka.actor.ActorRef;

public final class PriceRequest extends Message {
    public final ActorRef respondTo;
    public final String prodName;

    public PriceRequest(ActorRef respondTo, String prodName) {
        this.respondTo = respondTo;
        this.prodName = prodName;
    }
}
