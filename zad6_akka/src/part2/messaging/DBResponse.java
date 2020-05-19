package part2.messaging;

import java.util.UUID;

public class DBResponse {
    public final UUID requestID;
    public final int count;

    public DBResponse(UUID requestID, int count) {
        this.requestID = requestID;
        this.count = count;
    }
}
