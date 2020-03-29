package cosmic;

import com.rabbitmq.client.*;
import infra.Constants;
import infra.Job;
import infra.JobAck;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Provider extends Communicator {
    private String name;
    private String adminQueue;
    private String capability1;
    private String capability2;


    public Provider(String name, String capability1, String capability2) {
        this.name = name;
        this.capability1 = capability1;
        this.capability2 = capability2;
    }

    public void run() throws TimeoutException, IOException {
        Channel channel = this.createChannel();
        this.declareExchange(channel);
        this.declareQueues(channel);
        this.bindQueues(channel);

        channel.basicQos(1);
        System.out.printf("%s waiting for messages...\n", this.name);

        this.basicConsume(channel);
    }

    private void declareQueues(Channel channel) throws IOException {
        channel.queueDeclare(Constants.QUEUE_NAME_PERSON, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_CARGO, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_SATELLITE, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_ACK, false, false, false, null);

        this.adminQueue = channel.queueDeclare().getQueue();
    }

    private void bindQueues(Channel channel) throws IOException {
        this.bindQueue(channel, this.capability1);
        this.bindQueue(channel, this.capability2);

        channel.queueBind(this.adminQueue, Constants.EXCHANGE_GLOBAL, Constants.TOPIC_KEY_ADMIN_PROVIDER);
    }

    private void basicConsume(Channel channel) throws IOException {
        String providerName = this.name;
        Consumer taskConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, StandardCharsets.UTF_8);

                Job job = new Job(message);
                System.out.printf("Handling job: %s\n", job);

                // RabbitMQ ack
                channel.basicAck(envelope.getDeliveryTag(), false);

                // Ack to agency
                JobAck jobAck = new JobAck(providerName, job.capability, job.requestId);
                channel.basicPublish(Constants.EXCHANGE_GLOBAL, job.agencyName, null, jobAck.toString().getBytes());
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

        this.basicConsume(channel, capability1, taskConsumer);
        this.basicConsume(channel, capability2, taskConsumer);
        channel.basicConsume(this.adminQueue, false, adminConsumer);
    }

    private void bindQueue(Channel channel, String capability) throws IOException {
        if (capability.equals(Constants.TOPIC_KEY_PROVIDER_PERSON)) {
            channel.queueBind(Constants.QUEUE_NAME_PERSON, Constants.EXCHANGE_GLOBAL, capability);
        } else if (capability.equals(Constants.TOPIC_KEY_PROVIDER_CARGO)) {
            channel.queueBind(Constants.QUEUE_NAME_CARGO, Constants.EXCHANGE_GLOBAL, capability);
        } else if (capability.equals(Constants.TOPIC_KEY_PROVIDER_SATELLITE)) {
            channel.queueBind(Constants.QUEUE_NAME_SATELLITE, Constants.EXCHANGE_GLOBAL, capability);
        } else {
            throw new RuntimeException("How did you get here? No such capability");
        }
    }

    private void basicConsume(Channel channel, String capability, Consumer consumer) throws IOException {
        if (capability.equals(Constants.TOPIC_KEY_PROVIDER_PERSON)) {
            channel.basicConsume(Constants.QUEUE_NAME_PERSON, false, consumer);
        } else if (capability.equals(Constants.TOPIC_KEY_PROVIDER_CARGO)) {
            channel.basicConsume(Constants.QUEUE_NAME_CARGO, false, consumer);
        } else if (capability.equals(Constants.TOPIC_KEY_PROVIDER_SATELLITE)) {
            channel.basicConsume(Constants.QUEUE_NAME_SATELLITE, false, consumer);
        } else {
            throw new RuntimeException("How did you get here? No such capability");
        }
    }
}
