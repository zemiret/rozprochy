package src;

import grpc.types.NotifierGrpc;
import grpc.types.ObserveRequest;
import grpc.types.Empty;
import io.grpc.Channel;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.util.List;


// TODO: Niewywracalnosc
public class Client {
    private final NotifierGrpc.NotifierStub stub;
    private final NotifierGrpc.NotifierBlockingStub blockingStub;

    private Watchdog watchdog;

    private final StreamObserver<grpc.types.Event> requestObserver = new StreamObserver<>() {
        @Override
        public void onNext(grpc.types.Event event) {
            List<String> affectedList = event.getAffectedKingdomsList();

            StringBuilder builder = new StringBuilder();
            for (String s : affectedList) {
                builder.append(s).append(", ");
            }

            Log.info("Event, event, event!\n" +
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

            if (status.getCode().equals(Status.Code.UNAVAILABLE)) {
                watchdog.setServerDead();
                watchdog.startRecovery();
            }
        }

        @Override
        public void onCompleted() {
            Log.info("Server completed...");
        }
    };

    public Client(Channel channel) {
        stub = NotifierGrpc.newStub(channel);
        blockingStub = NotifierGrpc.newBlockingStub(channel);
    }

    public void setWatchdog(Watchdog watchdog) {
        this.watchdog = watchdog;
    }

    void observe(ObserveRequest request) {
        this.stub.observe(request, requestObserver);
    }

    Empty ping() {
        return blockingStub.ping(Empty.newBuilder().build());
    }
}
