package part2.messaging;

import java.util.UUID;

public final class PriceResponse extends Message {
    public final int price;
    public final UUID requestID;
    public int count;
    public String msg;
    public String prodName;

    public PriceResponse(UUID requestID, int price) {
        this.price = price;
        this.requestID = requestID;
        this.msg = "";
    }
}
