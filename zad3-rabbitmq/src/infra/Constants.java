package infra;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Constants {
    public static String TOPIC_KEY_PROVIDER_PERSON = "person";
    public static String TOPIC_KEY_PROVIDER_CARGO = "cargo";
    public static String TOPIC_KEY_PROVIDER_SATELLITE = "satellite";

    public static String TOPIC_KEY_ADMIN_PROVIDER = "admin.provider";
    public static String TOPIC_KEY_ADMIN_AGENCY = "admin.agency";

    public static String EXCHANGE_GLOBAL = "globalDirectExchange";

    public static String QUEUE_NAME_PERSON = "personQueue";
    public static String QUEUE_NAME_CARGO = "cargoQueue";
    public static String QUEUE_NAME_SATELLITE = "satelliteQueue";
    public static String QUEUE_NAME_ACK = "ackQueue";

    public static String FACTORY_HOST = "localhost";

    public static List<String> CAPABILITIES = new LinkedList<>(Arrays.asList(
            TOPIC_KEY_PROVIDER_CARGO,
            TOPIC_KEY_PROVIDER_PERSON,
            TOPIC_KEY_PROVIDER_SATELLITE
    ));
}
