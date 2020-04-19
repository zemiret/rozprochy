package src;

import grpc.types.Event;
import grpc.types.NotifierGrpc;
import grpc.types.ObserveRequest;
import grpc.types.EventType;
import grpc.types.EventSeverity;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;


// TODO: Niewywracalnosc

public class Client {
    private static final Logger logger = Logger.getLogger(Client.class.getName());
    private final NotifierGrpc.NotifierStub stub;

    public Client(Channel channel) {
        stub = NotifierGrpc.newStub(channel);
    }

    static void info(String msg, Object... params) {
        logger.log(Level.INFO, msg, params);
    }

    public static void main(String[] args) {
        String target = "localhost:20200";
        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        Client client = new Client(channel);

        info("Starting client...");

        StreamObserver<Event> requestObserver = new StreamObserver<Event>() {
                    @Override
                    public void onNext(Event event) {
                        List<String> affectedList = event.getAffectedKingdomsList();

                        StringBuilder builder = new StringBuilder();
                        for (String s : affectedList) {
                            builder.append(s).append(", ");
                        }

                        info("Event, event, event!\n" +
                                        "Type: {0} \n" +
                                        "Severity: {1}\n" +
                                        "Description: {2}\n" +
                                        "Affected Kingdoms: {3}\n",
                                event.getType(),
                                event.getSeverity(),
                                event.getDescription(),
                                builder.toString()
                        );
                    }

                    @Override
                    public void onError(Throwable t) {
                        Status status = Status.fromThrowable(t);
                        info("Server errored with: {0} ...", status);
                    }

                    @Override
                    public void onCompleted() {
                        info("Server completed...");
                    }
                };

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while(true) {
            try {
                System.out.println("-- SETTING NEW KINGDOM SUBSCRIPTION --");
                System.out.println("Kingdom to watch (empty to watch all kingdoms):");
                String kingdom = br.readLine();

                System.out.println("Event type to watch: ");
                System.out.println("0: army defeated");
                System.out.println("1: city destroyed");
                System.out.println("2: king changed");
                int evType = Integer.parseInt(br.readLine());

                System.out.println("Event severity to watch");
                System.out.println("0: low");
                System.out.println("1: high");
                int severity = Integer.parseInt(br.readLine());

                ObserveRequest request = ObserveRequest.newBuilder()
                        .setKingdom(kingdom)
                        .setType(EventType.forNumber(evType))
                        .setSeverity(EventSeverity.forNumber(severity))
                        .build();

                client.stub.observe(request, requestObserver);
            } catch (IOException | NumberFormatException e) {
                logger.log(Level.WARNING, "Readline failed");
            }
        }
    }
}
