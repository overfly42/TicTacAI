package MyTicTacAI2.RabbitMQ;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import MyTicTacAI2.Communication.ClientQueue;
import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Communication.ServerQueue;
import MyTicTacAI2.Interfaces.IChangeListener;

//@TestInstance(Lifecycle.PER_CLASS)
public class CommunicationTest implements IChangeListener {

    ServerQueue server;
    ClientQueue client1;
    ClientQueue client2;

    private class ComResult {
        public Message msg;
        Map<Keys, String> content;
    }

    private static List<ComResult> responses = new ArrayList<>();;

    public CommunicationTest() {
    }

    // @BeforeAll
    public void testInit() {
        responses.clear();
        server = new ServerQueue("server_in");
        client1 = new ClientQueue("c1", "server_in");
        client2 = new ClientQueue("c2", "server_in");
    }

    @ParameterizedTest
    @MethodSource("provideClientReceivings")
    public void testServerSendClientReceive(String receiver, int count) {
        System.out.println("=====================================");
        System.out.println(String.format("Starting test with %s as receiver and %d answers", receiver, count));
        testInit();

        // Initial Checks
        assertNotNull(server);
        assertNotNull(client1);
        assertNotNull(client2);

        // Preperation
        client1.addListener(this);
        client2.addListener(this);

        // Testexecution
        Map<Keys, String> content = new HashMap<>();
        content.put(Keys.ID, receiver);
        server.sendMessage(Message.RegisterOpen, content);

        // Testevaluation
        try {
            Thread.sleep(2000);

        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        System.out.println(
                String.format("Evaluating test with %s as receiver and %d answers", receiver, responses.size()));

        assertEquals(count, responses.size());
        for (int i = 0; i < count; i++)
            assertEquals(Message.RegisterOpen, responses.get(i).msg);
    }

    private static Stream<Arguments> provideClientReceivings() {
        return Stream.of(Arguments.of("all", 2), Arguments.of("c1", 1), Arguments.of("c2", 1));
    }

    

    @Override
    public void update() {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Message msg) {
        // TODO Auto-generated method stub

    }

    @Override
    public void update(Message msg, Map<Keys, String> content) {
        System.out.print("---Receive Message:");
        ComResult response = new ComResult();
        response.content = content;
        response.msg = msg;
        System.out.print(String.format("List contains %d elements before", responses.size()));
        responses.add(response);
        System.out.println(String.format(" and %d elements after adding", responses.size()));
    }
}