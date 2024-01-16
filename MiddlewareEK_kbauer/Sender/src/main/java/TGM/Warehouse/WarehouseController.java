package TGM.Warehouse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/warehouse")
public class WarehouseController {

    private final MOMSender momSender;

    @Autowired
    public WarehouseController(MOMSender momSender) {
        this.momSender = momSender;
    }

    @PostMapping("/send-data")
    public String sendData(@RequestParam String name, @RequestParam String city, @RequestParam String destination) {
        momSender.sendWarehouseDataToTopic(name, city);
        return "Data sent to " + destination;
    }
}
