package live.ioteatime.apiservice.adaptor;

import live.ioteatime.apiservice.dto.AddBrokerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "rule-engine")
public interface SensorAdaptor {

    @PostMapping("/brokers")
    ResponseEntity<String> addBrokers(@RequestBody AddBrokerRequest addBrokerRequest);

}
