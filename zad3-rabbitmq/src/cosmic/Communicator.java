package cosmic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import infra.Constants;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

abstract class Communicator {
    protected Channel createChannel() throws IOException, TimeoutException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Constants.FACTORY_HOST);
        Connection connection = factory.newConnection();
        return connection.createChannel();
    }

    protected void declareExchange(Channel channel) throws IOException {
        channel.exchangeDeclare(Constants.EXCHANGE_GLOBAL, BuiltinExchangeType.TOPIC);
    }
}
