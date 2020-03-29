package cosmic;

import com.rabbitmq.client.*;
import infra.Constants;
import infra.Job;
import infra.JobAck;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Agency extends Communicator {
    private String name;
    private String ackQueueName;
    private String adminQueueName;
    private long requestId;

    public Agency(String name) {
        this.name = name;
        this.requestId = 0;
        this.ackQueueName = Constants.QUEUE_NAME_ACK + this.name;
    }

    public void run() throws TimeoutException, IOException {
        System.out.printf("Agency %s starting...\n", name);

        Channel channel = this.createChannel();
        this.declareExchange(channel);
        this.declareQueues(channel);
        this.bindQueues(channel);
        this.basicConsume(channel);


        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

            System.out.println("Accepting messages...");
            String message;
            while ((message = br.readLine()) != null) {
                if (!Constants.CAPABILITIES.contains(message)) {
                    System.out.printf("Invalid job type: %s\n", message);
                    continue;
                }

                Job job = new Job(this.name, message, Long.toString(this.requestId));
                this.requestId++;

                System.out.printf("Sending job to key: %s\n", job.capability);
                channel.basicPublish(Constants.EXCHANGE_GLOBAL, job.capability, null, job.toString().getBytes());
            }
        } catch (Exception e) {
            System.out.printf("Stopping agent: %s\n", this.name);
        } finally {
            channel.queueUnbind(this.ackQueueName, Constants.EXCHANGE_GLOBAL, this.name);
            channel.queueDelete(this.ackQueueName);
        }
    }

    private void declareQueues(Channel channel) throws IOException {
        channel.queueDeclare(Constants.QUEUE_NAME_PERSON, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_CARGO, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_SATELLITE, false, false, false, null);

        channel.queueDeclare(this.ackQueueName, false, false, false, null);

        this.adminQueueName = channel.queueDeclare().getQueue();
    }

    private void bindQueues(Channel channel) throws IOException {
        channel.queueBind(this.ackQueueName, Constants.EXCHANGE_GLOBAL, this.name);
        channel.queueBind(this.adminQueueName, Constants.EXCHANGE_GLOBAL, Constants.TOPIC_KEY_ADMIN_AGENCY);
    }

    private void basicConsume(Channel channel) throws IOException {
        String agencyName = this.name;
        Consumer ackConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                JobAck jobAck = new JobAck(message);
                System.out.printf("Agency %s, received ack: %s\n", agencyName, jobAck);

                // RabbitMQ ack
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        Consumer adminConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);
                System.out.printf("Admin says: %s\n", message);

                // RabbitMQ ack
                channel.basicAck(envelope.getDeliveryTag(), false);
            }
        };

        channel.basicConsume(this.ackQueueName, false, ackConsumer);
        channel.basicConsume(this.adminQueueName, false, adminConsumer);
    }
}
