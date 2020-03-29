package cosmic;

import com.rabbitmq.client.*;
import infra.Constants;
import infra.Job;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;
import java.util.regex.Pattern;

public class Admin extends Communicator {
    private static String AGENCY_KEY = "agency";
    private static String PROVIDER_KEY = "provider";
    private static String ALL_KEY = "all";

    private String middlemanQueue;

    public void run() throws TimeoutException, IOException {
        Channel channel = this.createChannel();
        this.declareExchange(channel);
        this.declareAndBindQueues(channel);

        System.out.println("Starting admin...");

        this.basicConsume(channel);

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Admin waiting for your call! (in format provider|agency|all: Your message here)");
            String message;
            while ((message = br.readLine()) != null) {
                String[] split = message.split(Pattern.quote(":"));

                if (!isFormatValid(split)) {
                    System.out.println("Invalid format");
                    continue;
                }

                String key = split[0];
                StringBuilder toSendBuilder = new StringBuilder();
                for (int i = 1; i < split.length; ++i) {
                    toSendBuilder.append(split[i]);
                    if (i != split.length - 1) {
                        toSendBuilder.append(':');
                    }
                }

                byte[] toSend = toSendBuilder.toString().getBytes();

                if (key.equals(AGENCY_KEY)) {
                    channel.basicPublish(Constants.EXCHANGE_GLOBAL, Constants.TOPIC_KEY_ADMIN_AGENCY, null, toSend);
                } else if (key.equals(PROVIDER_KEY)) {
                    channel.basicPublish(Constants.EXCHANGE_GLOBAL, Constants.TOPIC_KEY_ADMIN_PROVIDER, null, toSend);
                } else if (key.equals(ALL_KEY)) {
                    channel.basicPublish(Constants.EXCHANGE_GLOBAL, Constants.TOPIC_KEY_ADMIN_PROVIDER, null, toSend);
                    channel.basicPublish(Constants.EXCHANGE_GLOBAL, Constants.TOPIC_KEY_ADMIN_AGENCY, null, toSend);
                } else {
                    System.out.println("How did you get here? Invalid admin msg key");
                }
            }
        } catch (Exception e) {
            System.out.println("Stopping admin");
        } finally {
            channel.queueUnbind(this.middlemanQueue, Constants.EXCHANGE_GLOBAL, "#");
            channel.queueDelete(this.middlemanQueue);
        }
    }

    private boolean isFormatValid(String[] split) {
        if (split.length < 2) {
            return false;
        }
        String sendTo = split[0];

        System.out.printf("Send to: %s\n", sendTo);

        return (sendTo.equals(PROVIDER_KEY) || sendTo.equals(AGENCY_KEY) || sendTo.equals(ALL_KEY));
    }

    private void declareAndBindQueues(Channel channel) throws IOException {
        this.middlemanQueue = channel.queueDeclare().getQueue();
        channel.queueBind(middlemanQueue, Constants.EXCHANGE_GLOBAL, "#");
    }

    private void basicConsume(Channel channel) throws IOException {
        Consumer middlemanConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.printf("Snooped message: %s\n", message);

                // RabbitMQ ack
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        channel.basicConsume(this.middlemanQueue, false, middlemanConsumer);
    }
}
