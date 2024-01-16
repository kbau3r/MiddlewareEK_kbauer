package TGM.Warehouse;

import jakarta.jms.JMSException;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import org.apache.tomcat.websocket.WsRemoteEndpointAsync;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RestController
public class JmsReceiver {

    @Autowired
    private JmsTemplate jmsTemplate;

    public static HashMap<Integer, String> log = new HashMap<>();

    public static String send = "[]";

    public static String appendObjectToJsonArray(String jsonArrayStr, String jsonObjectStr) {
        String newjsonArrayStr = "";
        if (jsonArrayStr.endsWith("]")) {
            newjsonArrayStr = jsonArrayStr.substring(0, jsonArrayStr.length() - 1);
        }

        if (jsonArrayStr.equals("[]")) {
            return "[" + jsonObjectStr + "]";
        } else {
            return newjsonArrayStr + "," + jsonObjectStr + "]";
        }
    }


    @JmsListener(destination = "SampleTopic")
    public void receiveMessage(String message) throws JMSException {
        String patternString = "\"warehouseID\":\"(-?\\d+)\"";

        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(message);

        send = appendObjectToJsonArray(send,message);

        if (matcher.find()) {
            log.put(Integer.parseInt(matcher.group(1)), message);
        } else {
            System.out.println("Kein Warehouse ID im Message gefunden");
        }

        System.out.println("//");
        System.out.println(message);

        Topic topic = jmsTemplate.getConnectionFactory().createConnection()
                .createSession(false, Session.AUTO_ACKNOWLEDGE)
                .createTopic("bestaetigungTopic");

        jmsTemplate.setPubSubDomain(true);
        jmsTemplate.convertAndSend("bestaetigungTopic", Integer.parseInt(matcher.group(1)));
        jmsTemplate.setPubSubDomain(false);


    }

    @GetMapping(value = "/get-data", produces = MediaType.APPLICATION_JSON_VALUE)
    public String sendData() {
        return send;
    }
}
