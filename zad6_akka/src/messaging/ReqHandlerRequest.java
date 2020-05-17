package messaging;

import akka.actor.ActorRef;

public class ReqHandlerRequest {
    public final ActorRef store1;
    public final ActorRef store2;
    public final PriceRequest priceRequest;

    public ReqHandlerRequest(ActorRef store1, ActorRef store2, PriceRequest priceRequest) {
        this.store1 = store1;
        this.store2 = store2;
        this.priceRequest = priceRequest;
    }
}
