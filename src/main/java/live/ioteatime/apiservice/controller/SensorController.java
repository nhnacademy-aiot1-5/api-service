package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.Sensor;
import live.ioteatime.apiservice.dto.AddSensorRequest;
import live.ioteatime.apiservice.dto.TopicDto;
import live.ioteatime.apiservice.service.SensorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping
@Tag(name = "센서 컨트롤러", description = "센서 사용할 수 있는 컨트롤러입니다.")
@RequiredArgsConstructor
public class SensorController {

    private final static String X_USER_ID = "X-USER-ID";
    private final SensorService sensorService;

    /**
     * 어드민만 사용할 수 있는 명령어이며 연결된 센서의 리스트를 가져오는 컨트롤러다.
     * @return 연결된 센서들의 리스트를 반환한다.
     */
    @GetMapping("/sensors")
    @AdminOnly
    @Operation(summary = "모든 센서들의 리스트를 가져오는 API", description = "모든 센서들의 리스트를 가져옵니다.")
    public ResponseEntity<List<Sensor>> getSensors(){
        return ResponseEntity.ok(sensorService.getSensors());
    }


    /**
     * 어드민만 사용할 수 있는 명령어이며 새 센서를 추가하는 핸들러다.
     * @return 등록 완료한 센서 아이디
     */
    @PostMapping("/sensor")
    @AdminOnly
    @Operation(summary = "MQTT 센서를 추가하는 API", description = "MQTT 센서를 추가합니다.")
    public ResponseEntity<String> addMqttSensor(@RequestHeader(X_USER_ID) String userId, @RequestBody AddSensorRequest addSensorRequest){
        int registeredSensorId = sensorService.addMqttSensor(userId, addSensorRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body("Created. id="+registeredSensorId);
    }

    @PostMapping("/topic")
    @AdminOnly
    @Operation(summary = "토픽을 추가하는 API", description = "센서의 채널에 대한 토픽을 추가합니다.")
    public ResponseEntity<String> addTopic(@RequestHeader(X_USER_ID) String userId, @RequestBody TopicDto topicDto) {

        String addedTopic = null;

        return ResponseEntity.status(HttpStatus.CREATED).body("Added topic : " + addedTopic);
    }

    @PutMapping("/update-topic")
    @AdminOnly
    @Operation(summary = "토픽을 수정하는 API", description = "센서 토픽을 수정합니다.")
    public ResponseEntity<String> updateTopic(@RequestHeader(X_USER_ID) String userId, @RequestBody TopicDto topicDto){

        String updatedTopic = null;

        return ResponseEntity.ok("Updated topic : " + updatedTopic);
    }
}
