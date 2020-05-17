package messaging;

import java.util.UUID;

public class TimeoutMessage {
    public final UUID requestId;

    public TimeoutMessage(UUID requestId) {
        this.requestId = requestId;
    }
}
