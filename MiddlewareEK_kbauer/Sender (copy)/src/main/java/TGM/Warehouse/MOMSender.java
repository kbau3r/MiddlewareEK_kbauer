package TGM.Warehouse;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.Session;
import jakarta.jms.Topic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Component
public class MOMSender {



    private static Map<Integer, String> sent = new HashMap<>();
    private static Set<Integer> aknowledged = new HashSet<>();

    private final JmsTemplate jmsTemplate;
    private final ObjectMapper objectMapper;
    private final XMLGenerator xmlGenerator;

    @Autowired
    public MOMSender(JmsTemplate jmsTemplate, ObjectMapper objectMapper, XMLGenerator xmlGenerator) {
        this.jmsTemplate = jmsTemplate;
        this.objectMapper = objectMapper;
        this.xmlGenerator = xmlGenerator;
    }

    @JmsListener(destination = "bestaetigungTopic", containerFactory = "myJmsContainerFactory")
    public void receiveAck(String ackMessage) {
        if (sent.containsKey(Integer.parseInt(ackMessage))){
            aknowledged.add(Integer.parseInt(ackMessage));
            System.out.println("//\n//");
            System.out.println(ackMessage);
        }
    }



    public void sendWarehouseDataToTopic(String name, String city) {
        try {
            String patternString = "\"warehouseID\":\"(-?\\d+)\"";
            Pattern pattern = Pattern.compile(patternString);
            String xmlData = xmlGenerator.getJSON(name, city);
            Matcher matcher = pattern.matcher(xmlData);
            Topic topic = jmsTemplate.getConnectionFactory().createConnection()
                    .createSession(false, Session.AUTO_ACKNOWLEDGE)
                    .createTopic("SampleTopic");
            jmsTemplate.convertAndSend(topic, xmlData);
            matcher.find();
            sent.put( Integer.parseInt(matcher.group(1)), xmlData);


        } catch (Exception e) {
            throw new RuntimeException("Fehler beim Senden der WarehouseData an das Topic", e);
        }
    }
}
