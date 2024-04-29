package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.annotation.ValidOrganization;
import live.ioteatime.apiservice.dto.TopicDto;
import live.ioteatime.apiservice.dto.TopicRequest;
import live.ioteatime.apiservice.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors")
@Tag(name = "토픽 컨트롤러", description = "토픽 관리에 사용하는 컨트롤러입니다.")
public class TopicController {
    private final TopicService topicService;
    private static final String X_USER_ID = "X-USER-ID";

    /**
     * 센서별 토픽리스트를 조회합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @return 200 OK
     */
    @GetMapping("/{sensorId}/topics")
    @ValidOrganization
    public ResponseEntity<List<TopicDto>> getTopicsBySensorId(@RequestHeader(X_USER_ID) String userId,
                                                              @PathVariable("sensorId") int sensorId) {
        List<TopicDto> topicDtoList = topicService.getTopicsBySensorId(sensorId);
        return ResponseEntity.ok(topicDtoList);
    }

    /**
     * 토픽을 단일 조회합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param topicId 토픽아이디
     * @return 성공 - 200 OK, 실패- 404 NOT FOUND
     */
    @GetMapping("/{sensorId}/topics/{topicId}")
    @ValidOrganization
    public ResponseEntity<TopicDto> getTopicByTopicId(@RequestHeader(X_USER_ID) String userId,
                                                      @PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId){
        TopicDto topicDto = topicService.getTopicByTopicId(topicId);
        return ResponseEntity.ok(topicDto);
    }

    /**
     * mqtt 센서의 토픽을 추가합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param topicDto 토픽과 설명
     * @return 200 Ok
     */
    @PostMapping("/{sensorId}/topics")
    @AdminOnly @ValidOrganization
    @Operation(summary = "토픽 추가 API",description = "MQTT 센서의 토픽을 추가하는 API입니다.")
    public ResponseEntity<String> addTopic(@RequestHeader(X_USER_ID) String userId,
                                           @PathVariable("sensorId") int sensorId, @RequestBody TopicDto topicDto) {

        String addedTopic = topicService.addTopic(sensorId, topicDto);

        return ResponseEntity.ok(addedTopic);
    }

    /**
     * 토픽을 수정합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param topicId 토픽아이디
     * @param topicRequest 토픽 수정 요청
     * @return 200 Ok
     */
    @PutMapping("/{sensorId}/topics/{topicId}/update")
    @AdminOnly @ValidOrganization
    public ResponseEntity<String> updateTopic(@RequestHeader(X_USER_ID) String userId,
                                              @PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId,
                                              @RequestBody TopicRequest topicRequest){
        topicService.updateTopic(topicId, topicRequest);
        return ResponseEntity.ok().body("Updated topic. id="+topicId);
    }

    /**
     * mqtt 센서의 토픽을 삭제합니다.
     * @param userId 유저아이디
     * @param sensorId 센서아이디
     * @param topicId 삭제할 토픽아이디
     * @return 204 No Content
     */
    @DeleteMapping("/{sensorId}/topics/{topicId}")
    @AdminOnly @ValidOrganization
    public ResponseEntity<String> deleteTopic(@RequestHeader(X_USER_ID) String userId,
                                              @PathVariable("sensorId") int sensorId, @PathVariable("topicId") int topicId){

        topicService.deleteTopic(sensorId, topicId);

        return ResponseEntity.noContent().build();
    }

}
