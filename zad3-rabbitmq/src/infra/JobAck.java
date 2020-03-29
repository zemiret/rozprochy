package infra;

import java.util.regex.Pattern;

public class JobAck {
    public String providerName;
    public String requestId;

    public JobAck(String providerName, String capability, String requestId) {
        this.providerName = providerName;
        this.requestId = requestId;
    }

    public JobAck(String encoded) {
        String[] parts = encoded.split(Pattern.quote("."));

        this.providerName = parts[0];
        this.requestId = parts[1];
    }

    @Override
    public String toString() {
        return this.providerName + '.' +
                this.requestId;
    }
}
