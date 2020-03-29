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

public class Agency {
    private String name;
    private long requestId;

    public Agency(String name) {
        this.name = name;
        this.requestId = 0;
    }

    public void run() throws TimeoutException, IOException {
        System.out.printf("Agency %s starting\n", name);

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.FACTORY_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Constants.EXCHANGE_GLOBAL_DIRECT, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(Constants.QUEUE_NAME_PERSON, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_CARGO, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_SATELLITE, false, false, false, null);

        channel.queueDeclare(Constants.QUEUE_NAME_ACK + this.name, false, false, false, null);


        System.out.printf("Binding to key: %s\n", this.name);
        channel.queueBind(Constants.QUEUE_NAME_ACK + this.name, Constants.EXCHANGE_GLOBAL_DIRECT, this.name);

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

        channel.basicConsume(Constants.QUEUE_NAME_ACK + this.name, false, ackConsumer);

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
                channel.basicPublish(Constants.EXCHANGE_GLOBAL_DIRECT, job.capability, null, job.toString().getBytes());
            }
        } catch (Exception e) {
            System.out.printf("Stopping agent: %s\n", this.name);
        } finally {
            channel.queueUnbind(Constants.QUEUE_NAME_ACK + this.name, Constants.EXCHANGE_GLOBAL_DIRECT, this.name);
        }
    }
}
