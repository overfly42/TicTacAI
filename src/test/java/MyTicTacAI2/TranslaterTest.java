package MyTicTacAI2;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

import MyTicTacAI2.Communication.Keys;
import MyTicTacAI2.Communication.Message;
import MyTicTacAI2.Communication.Translator;

public class TranslaterTest {
    private class DataTuple {
        String rawMessage;
        Message messageType;
        Map<Keys, String> messageContent = new HashMap<>();
    }

    List<DataTuple> testData;

    public TranslaterTest() {
        testData = new ArrayList<>();
        initMessages();
    }

    private void initMessages() {
        DataTuple payload;

        payload = new DataTuple();
        testData.add(payload);
        payload.rawMessage = "StartSession:ID=5";
        payload.messageType = Message.StartSession;
        payload.messageContent.put(Keys.ID, "5");

        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "RegisterOpen";
        payload.messageType = Message.RegisterOpen;

        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "Register:ID=P1";
        payload.messageType = Message.Register;
        payload.messageContent.put(Keys.ID, "P1");
        
        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "RegisterSuccess:ID=P1";
        payload.messageType = Message.RegisterSuccess;
        payload.messageContent.put(Keys.ID, "P1");

        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "RegisterRejected:ID=P1";
        payload.messageType = Message.RegisterRejected;
        payload.messageContent.put(Keys.ID, "P1");

        payload = new DataTuple();
        testData.add(payload);
        payload.rawMessage = "StartGame:ID=all:PlayerA=P1:PlayerB=P2";
        payload.messageType = Message.StartGame;
        payload.messageContent.put(Keys.ID, "all");
        payload.messageContent.put(Keys.PlayerA,"P1");
        payload.messageContent.put(Keys.PlayerB,"P2");

        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "PlayerReady:ID=P1";
        payload.messageType = Message.PlayerReady;
        payload.messageContent.put(Keys.ID, "P1");

        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "Turn:ID=P1";
        payload.messageType = Message.Turn;
        payload.messageContent.put(Keys.ID, "P1");

        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "Set:ID=P1:X=1:Y=2";
        payload.messageType = Message.Set;
        payload.messageContent.put(Keys.ID, "P1");
        payload.messageContent.put(Keys.X, "1");
        payload.messageContent.put(Keys.Y, "2");

        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "SetRejected:ID=P1";
        payload.messageType = Message.SetRejected;
        payload.messageContent.put(Keys.ID, "P1");

        payload =new DataTuple();
        testData.add(payload);
        payload.rawMessage = "EndGame:ID=all:PlayerA=5:PlayerB=2:Tie=200";
        payload.messageType = Message.EndGame;
        payload.messageContent.put(Keys.ID, "all");
        payload.messageContent.put(Keys.PlayerA, "5");
        payload.messageContent.put(Keys.PlayerB, "2");
        payload.messageContent.put(Keys.Tie, "200");
        


    }

    @Test
    public void testToQueue() {
        for (DataTuple test : testData) {
            String result = Translator.toQueue(test.messageType, test.messageContent);
            assertEquals(test.rawMessage, result, "MessageType="+test.messageType);
        }
    }

    @Test
    public void testFromQueue() {
        Map<Keys, String> resultContent = new HashMap<>();
        for (DataTuple test : testData) {
            resultContent.clear();
            Message resultMsg = Translator.fromQueue(test.rawMessage, resultContent);
            assertEquals(test.messageType, resultMsg);
            assertEquals(test.messageContent, resultContent, "MessageType="+test.messageType);
        }
    }
}