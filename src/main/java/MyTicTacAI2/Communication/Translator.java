package MyTicTacAI2.Communication;

import java.util.Map;

public class Translator {
    public static String toQueue(Message msg, Map<Keys, String> content) {
        String result;
        switch (msg) {
            case Register:
            case RegisterRejected:
                result = content.get(Keys.ID) + ":" + msg.toString();
                break;
            case RegisterOpen:
                result = "all:" + msg.toString();
                break;
            default:
                result = String.format("Message Type ({0}) not implemented from Server Side", msg.toString());
        }
        return result;
    }

    /**
     * 
     * @param message
     * @param content is an out parameter and should be initialized but empty
     * @return
     */
    public static Message fromQueue(String message, Map<Keys, String> content) {
        var x = message.split(":");
        content.put(Keys.ID, x[0]);
        var e = Enum.valueOf(Message.class, x[1]);
        return e;
    }
}