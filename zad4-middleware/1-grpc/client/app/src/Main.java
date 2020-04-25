package src;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import grpc.types.ObserveRequest;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) {
        String target = "localhost:20200";

        ManagedChannel channel = ManagedChannelBuilder.forTarget(target).usePlaintext().build();
        Client client = new Client(channel);
        PersistentStore<ObserveRequest> store = new PersistentStore<>();

        Watchdog watchdog = new Watchdog(store, client);
        client.setWatchdog(watchdog);

        Log.info("Starting client...");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
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

                grpc.types.ObserveRequest request = grpc.types.ObserveRequest.newBuilder()
                        .setKingdom(kingdom)
                        .setType(grpc.types.EventType.forNumber(evType))
                        .setSeverity(grpc.types.EventSeverity.forNumber(severity))
                        .build();

                store.add(request);
                client.observe(request);

                if (!watchdog.isServerAlive()) {
                    Log.info("Server is dead. Your subscription will be added once it's back.");
                }
            } catch (Exception e) {
                Log.warning("Exception in main loop: " + e.getMessage());
            }
        }
    }
}
