package live.ioteatime.apiservice.adaptor;

import live.ioteatime.apiservice.dto.AddModbusSensorRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(value = "rule-engine", contextId = "modbus-broker")
public interface ModbusSensorAdaptor {
    @PostMapping("/modbus")
    ResponseEntity<String> addModbusSensor(@RequestBody AddModbusSensorRequest addSensorRequest);
}
