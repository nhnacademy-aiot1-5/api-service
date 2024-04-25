package live.ioteatime.apiservice.adaptor;

import live.ioteatime.apiservice.dto.AddBrokerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@FeignClient(value = "rule-engine")
public interface SensorAdaptor {

    @PostMapping("/brokers")
    ResponseEntity<String> addBrokers(@RequestBody AddBrokerRequest addBrokerRequest);

    @PostMapping("/brokers/{brokerId}/topics")
    ResponseEntity<String> addTopics(@PathVariable("brokerId") String brokerId, @RequestBody Map<String, String> topic);

    @DeleteMapping("/brokers/{brokerId}/topics")
    ResponseEntity<String> deleteTopics(@PathVariable("brokerId") String brokerId, @RequestBody Map<String, String> topicRequest);

}
