package infra;

import java.util.regex.Pattern;

public class Job {
    public String agencyName;
    public String capability;
    public String requestId;

    public Job(String agencyName, String capability, String requestId) {
        this.agencyName = agencyName;
        this.capability = capability;
        this.requestId = requestId;
    }

    public Job(String encoded) {
        String[] parts = encoded.split(Pattern.quote("."));

        this.agencyName = parts[0];
        this.capability = parts[1];
        this.requestId = parts[2];
    }

    @Override
    public String toString() {
        return this.agencyName + '.' +
                this.capability + '.' +
                this.requestId;
    }
}
