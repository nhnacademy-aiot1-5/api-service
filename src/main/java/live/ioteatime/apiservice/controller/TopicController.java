package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.dto.TopicDto;
import live.ioteatime.apiservice.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors")
@Tag(name = "토픽 컨트롤러", description = "토픽 관리에 사용하는 컨트롤러입니다.")
public class TopicController {
    private final TopicService topicService;
    private static final String X_USER_ID = "X-USER-ID";

    /**
     * mqtt 센서의 토픽을 추가합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param topicDto 토픽과 설명
     * @return 200 Ok
     */
    @PostMapping("{sensorId}/topics")
    @AdminOnly
    public ResponseEntity<String> addTopic(@RequestHeader(X_USER_ID) String userId,
                                           @PathVariable("sensorId") int sensorId, @RequestBody TopicDto topicDto) {

        String addedTopic = topicService.addTopic(sensorId, topicDto);

        return ResponseEntity.ok(addedTopic);
    }

    /**
     * mqtt 센서의 토픽을 삭제합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param topicId 삭제할 토픽아이디
     * @return 204 No Content
     */
    @DeleteMapping("{sensorId}/topics/{topicId}")
    @AdminOnly
    public ResponseEntity<String> deleteTopic(@RequestHeader(X_USER_ID) String userId,
                                              @PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId){

        topicService.deleteTopic(sensorId, topicId);

        return ResponseEntity.noContent().build();
    }

}
