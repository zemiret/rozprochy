package src;

import grpc.types.ObserveRequest;
import grpc.types.Empty;

import java.util.concurrent.TimeUnit;


public class Watchdog {
    private ServerStatus serverStatus;
    private final PersistentStore<ObserveRequest> store;
    private final Client client;
    private boolean inRecoveryMode = false;

    public Watchdog(PersistentStore<ObserveRequest> store, Client client) {
        this.serverStatus = ServerStatus.ALIVE;
        this.store = store;
        this.client = client;
    }

    void startRecovery() {
        if (store.isEmpty()) {
            return;
        }

        if (inRecoveryMode) { // already recovering
            return;
        }

        this.inRecoveryMode = true;

        Log.warning("Server died, starting recovery...");

        while (serverStatus == ServerStatus.DEAD) {
            try {
                TimeUnit.MILLISECONDS.sleep(100);
                Empty response = client.ping();
            } catch (Exception e) {
                continue;
            }

            // TODO: FOR SOME REASON, EACH TIME WE RECOVER, WE GET +1 DUPLICATE OF THE MESSAGE
            for (ObserveRequest req: store.getEntities()) {
                client.observe(req);
            }

            this.inRecoveryMode = false;
            Log.warning("Server is back...");
            serverStatus = ServerStatus.ALIVE;
        }
    }

    boolean isServerAlive() {
        return serverStatus == ServerStatus.ALIVE;
    }

    void setServerDead() {
        this.serverStatus = ServerStatus.DEAD;
    }
}
