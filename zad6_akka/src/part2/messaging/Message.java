package part2.messaging;

import java.util.UUID;

public class Message {
    public final UUID id;

    Message() {
        this.id = UUID.randomUUID();
    }
}
