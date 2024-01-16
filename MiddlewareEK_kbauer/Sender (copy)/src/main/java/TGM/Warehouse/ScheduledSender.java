package TGM.Warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Component
public class ScheduledSender {



    private final MOMSender momSender;

    @Autowired
    public ScheduledSender(MOMSender momSender) {
        this.momSender = momSender;
    }

    public static String generateRandomString(int minLength, int maxLength) {
        Random random = new Random();
        int length = random.nextInt(maxLength - minLength + 1) + minLength;
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            // Generate a random lowercase letter
            char randomChar = (char) ('a' + random.nextInt(26));
            sb.append(randomChar);
        }

        return sb.toString();
    }

    @Scheduled(fixedRate = 5000) // every 5 seconds
    public void sendMessagesPeriodically() {
        String name =  generateRandomString(1,10);
        String city = generateRandomString(1,10);
        momSender.sendWarehouseDataToTopic(name, city);
    }
}
