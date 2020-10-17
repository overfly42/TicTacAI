package MyTicTacAI2.Communication;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

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
            rxChannel.exchangeDeclare(receivingQueue, "fanout");
            rxChannel.queueDeclare(receivingQueue, false, false, false, null);
            rxChannel.queueBind(receivingQueue, receivingQueue, "");

        } catch (Exception e) {
            // TODO: handle exception
        }

    }

}