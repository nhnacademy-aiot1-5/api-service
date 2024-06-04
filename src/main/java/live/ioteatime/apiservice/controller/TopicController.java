package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.dto.topic.TopicDto;
import live.ioteatime.apiservice.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors/mqtt/{sensorId}/topics")
@Tag(name = "Topic", description = "MQTT 센서 토픽 API")
public class TopicController {
    private final TopicService topicService;

    /**
     * 센서의 전체 토픽 리스트를 조회하는 메서드
     * @param sensorId 센서 아이디
     * @return 200 OK, 토픽 리스트
     */
    @GetMapping
    @Operation(summary = "전체 토픽 리스트 조회", description = "센서의 전체 토픽 리스트를 조회합니다.")
    public ResponseEntity<List<TopicDto>> getTopicsBySensorId(@PathVariable("sensorId") int sensorId) {
        return ResponseEntity.status(HttpStatus.OK).body(topicService.getTopicsBySensorId(sensorId));
    }

    /**
     * 토픽을 단일 조회하는 메서드
     * @param topicId 토픽아이디
     * @return 200 OK, 토픽 정보
     */
    @GetMapping("/{topicId}")
    @Operation(summary = "토픽 단일 조회", description = "토픽을 단일 조회합니다.")
    public ResponseEntity<TopicDto> getTopicByTopicId(@PathVariable("topicId") int topicId){
        return ResponseEntity.status(HttpStatus.OK).body(topicService.getTopicByTopicId(topicId));
    }

    /**
     * 토픽을 등록하는 메서드
     * @param sensorId 센서 아이디
     * @param topicDto 등록할 토픽 정보
     * @return 201 CREATED, 등록된 토픽 아이디
     */
    @PostMapping
    @AdminOnly
    @Operation(summary = "토픽 추가",description = "MQTT 센서의 토픽을 추가합니다.")
    public ResponseEntity<String> createTopic(@PathVariable("sensorId") int sensorId, @RequestBody TopicDto topicDto) {
        int createdTopicId = topicService.addTopic(sensorId, topicDto);

        URI location = UriComponentsBuilder
                .fromUriString("https://www.ioteatime.live/sensors/" + sensorId + "/topics/" + createdTopicId)
                .build().toUri();

        return ResponseEntity.status(HttpStatus.CREATED)
                .location(location).body("Topic registered: topicId=" + createdTopicId);
    }

    /**
     * 토픽을 수정하는 메서드
     * @param sensorId 센서 아이디
     * @param topicId 토픽 아이디
     * @param topicRequest 토픽 수정 요청
     * @return 200 OK, 수정된 토픽 아이디
     */
    @PutMapping("/{topicId}")
    @AdminOnly
    @Operation(summary = "토픽 수정", description = "토픽과 토픽에 대한 설명을 수정합니다.")
    public ResponseEntity<String> updateTopic(@PathVariable("sensorId") int sensorId,
                                              @PathVariable("topicId") int topicId,
                                              @RequestBody TopicDto topicRequest){
        topicService.updateTopic(sensorId, topicId, topicRequest);
        return ResponseEntity.status(HttpStatus.OK).body("Topic updated: topicId=" + topicId);
    }

    /**
     * 토픽을 삭제하는 메서드
     * @param sensorId 센서 아이디
     * @param topicId 삭제할 토픽 아이디
     * @return 204 NO CONTENT
     */
    @DeleteMapping("/{topicId}")
    @AdminOnly
    @Operation(summary = "토픽 삭제", description = "토픽을 삭제합니다.")
    public ResponseEntity<String> deleteTopic(@PathVariable("sensorId") int sensorId,
                                              @PathVariable("topicId") int topicId){
        topicService.deleteTopic(sensorId, topicId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
