package MyTicTacAI2.Communication;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class ClientQueue extends BaseQueue {
    
    public ClientQueue(String QueueNameReceive,String QueueNameSend)
    {
        super(QueueNameReceive,QueueNameSend);
    }
    protected void setupChannel()
    {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();

            txChannel = connection.createChannel();
            txChannel.queueDeclare(sendingExchangeName, false, false, false, null);

            rxChannel = connection.createChannel();
            rxChannel.queueDeclare(receivingQueue, false, false, false, null);

            rxChannel.queueBind(receivingQueue, EXCHANGE_NAME, receivingQueue);
            rxChannel.queueBind(receivingQueue, EXCHANGE_NAME, BORADCAST_KEY);
  
        } catch (Exception e) {
            //TODO: handle exception
        }
    }
}