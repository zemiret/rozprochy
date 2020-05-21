package part3;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import part3.messaging.ClientRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class App {
    public static void main(String[] args) throws Exception {
        final ActorSystem system = ActorSystem.create("local_system");

        final ActorRef client = system.actorOf(Props.create(ClientActor.class), "client");
        final ActorRef server = system.actorOf(Props.create(ServerActor.class), "server");

        System.out.println("Starting...");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            String line = br.readLine();
            if (line.equals("q")) {
                break;
            }

            client.tell(new ClientRequest(server, line), null);
        }

        system.terminate();
    }
}
