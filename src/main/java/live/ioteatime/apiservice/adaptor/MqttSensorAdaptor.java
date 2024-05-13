package live.ioteatime.apiservice.adaptor;

import live.ioteatime.apiservice.dto.AddBrokerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "rule-engine", contextId = "mqtt-broker")
public interface MqttSensorAdaptor {

    @PostMapping("/mqtt")
    ResponseEntity<String> addMqttBrokers(@RequestBody AddBrokerRequest addBrokerRequest);

    @GetMapping("/delete/{bridgeName}")
    ResponseEntity<String> deleteSensor(@PathVariable("bridgeName") String sensorId);

}
