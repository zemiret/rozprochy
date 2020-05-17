package messaging;

import akka.actor.ActorRef;

public final class ClientRequest extends Message {
    public final ActorRef server;
    public final String prodName;

    public ClientRequest(ActorRef server, String prodName) {
        this.server = server;
        this.prodName = prodName;
    }
}
