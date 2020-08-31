package MyTicTacAI2.Communication;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Connection;

public class ServerQueue extends BaseQueue {

    public ServerQueue(String QueueName) {
        super(QueueName, EXCHANGE_NAME);
    }

    @Override
    protected void setupChannel() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();

            txChannel = connection.createChannel();
            txChannel.exchangeDeclare(EXCHANGE_NAME, "direct");

            rxChannel = connection.createChannel();
            rxChannel.queueDeclare(receivingQueue, false, false, false, null);

        } catch (Exception e) {
            // TODO: handle exception
        }

    }
}