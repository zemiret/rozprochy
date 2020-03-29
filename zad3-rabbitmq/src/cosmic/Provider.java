package cosmic;

import com.rabbitmq.client.*;
import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;
import infra.Constants;
import infra.Job;
import infra.JobAck;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Provider {
    private String name;
    private String capability1;
    private String capability2;


    public Provider(String name, String capability1, String capability2) {
        this.name = name;
        this.capability1 = capability1;
        this.capability2 = capability2;
    }

    public void run() throws TimeoutException, IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.FACTORY_HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(Constants.EXCHANGE_GLOBAL_DIRECT, BuiltinExchangeType.DIRECT);

        channel.queueDeclare(Constants.QUEUE_NAME_PERSON, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_CARGO, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_SATELLITE, false, false, false, null);
        channel.queueDeclare(Constants.QUEUE_NAME_ACK, false, false, false, null);

        System.out.println("Binding to direct: ");
        System.out.println(capability1);
        System.out.println(capability2);

        this.bindQueue(channel, capability1);
        this.bindQueue(channel, capability2);

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

                System.out.printf("Sending ack to: %s\n", job.agencyName);

                channel.basicPublish(Constants.EXCHANGE_GLOBAL_DIRECT, job.agencyName, null, jobAck.toString().getBytes());
            }
        };

        channel.basicQos(1);
        System.out.printf("%s waiting for messages...\n", this.name);

        this.basicConsume(channel, capability1, taskConsumer);
        this.basicConsume(channel, capability2, taskConsumer);
    }

    private void bindQueue(Channel channel, String capability) throws IOException {
        if (capability.equals(Constants.TOPIC_KEY_PROVIDER_PERSON)) {
            System.out.println("Binding to person queue");
            channel.queueBind(Constants.QUEUE_NAME_PERSON, Constants.EXCHANGE_GLOBAL_DIRECT, capability);
        } else if (capability.equals(Constants.TOPIC_KEY_PROVIDER_CARGO)) {
            System.out.println("Binding to cargo queue");
            channel.queueBind(Constants.QUEUE_NAME_CARGO, Constants.EXCHANGE_GLOBAL_DIRECT, capability);
        } else if (capability.equals(Constants.TOPIC_KEY_PROVIDER_SATELLITE)) {
            System.out.println("Binding to satelite queue");
            channel.queueBind(Constants.QUEUE_NAME_SATELLITE, Constants.EXCHANGE_GLOBAL_DIRECT, capability);
        } else {
            throw new RuntimeException("How did you get here? No such capability");
        }
    }

    private void basicConsume(Channel channel, String capability, Consumer consumer) throws IOException {
        if (capability.equals(Constants.TOPIC_KEY_PROVIDER_PERSON)) {
            System.out.println("Consume to person queue");
            channel.basicConsume(Constants.QUEUE_NAME_PERSON, false, consumer);
        } else if (capability.equals(Constants.TOPIC_KEY_PROVIDER_CARGO)) {
            System.out.println("Consume to cargo queue");
            channel.basicConsume(Constants.QUEUE_NAME_CARGO, false, consumer);
        } else if (capability.equals(Constants.TOPIC_KEY_PROVIDER_SATELLITE)) {
            System.out.println("Consume to satelite queue");
            channel.basicConsume(Constants.QUEUE_NAME_SATELLITE, false, consumer);
        } else {
            throw new RuntimeException("How did you get here? No such capability");
        }
    }
}
