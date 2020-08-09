package MyTicTacAI2.Communication;

import java.util.Map;

public class Translator {
    public static String toQueue(Message msg, Map<Keys, String> content) {
        String result;
        switch (msg) {
            case Register:
            case RegisterRejected:
            case RegisterSuccess:
            case StartSession:
            case Turn:
            case StartGame:
            case PlayerReady:
            case Set:
            case SetRejected:
            case EndGame:
            case RegisterOpen:
                result = msg.toString() + convertMapToString(content);
                break;
            // case RegisterOpen:
            //     result = msg.toString();
            //     break;
            default:
                result = String.format("Message Type (%s) not implemented from Server Side", msg.toString());
        }
        return result;
    }

    private static String convertMapToString(Map<Keys, String> content) {
        String msg = "";
        for (Keys key : Keys.values()) {
            if (content.containsKey(key))
                msg += String.format(":%s=%s", key.toString(), content.get(key));
        }
        // msg = (content.size() == 0 ? "" : ":") + msg;
        return msg;
    }

    /**
     * 
     * @param message
     * @param content is an out parameter and should be initialized but empty
     * @return
     */
    public static Message fromQueue(String message, Map<Keys, String> content) {
        // if (!message.contains(":"))
        // return null;
        var x = message.split(":");
        // if (x.length >= 2)
        // content.put(Keys.ID, x[1]);
        var e = Enum.valueOf(Message.class, x[0]);
        for (int i = 1; i < x.length; i++) {
            if (!x[i].contains("="))
                continue;
            // Example for Test: "Set:e:X=0:Y=0"
            var part = x[i].split("=");
            content.put(Enum.valueOf(Keys.class, part[0]), part[1]);
        }
        return e;
    }
}