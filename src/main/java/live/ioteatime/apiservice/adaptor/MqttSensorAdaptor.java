package live.ioteatime.apiservice.adaptor;

import live.ioteatime.apiservice.dto.AddBrokerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "rule-engine", contextId = "mqtt-broker")
public interface MqttSensorAdaptor {

    @PostMapping("/mqtt")
    ResponseEntity<String> addMqttBrokers(@RequestBody AddBrokerRequest addBrokerRequest);

}
