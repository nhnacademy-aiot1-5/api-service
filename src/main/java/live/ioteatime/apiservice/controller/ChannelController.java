package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.dto.channel.ChannelDto;
import live.ioteatime.apiservice.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 채널과 관련된 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors/modbus")
public class ChannelController {
    private final ChannelService channelService;

    /**
     * 센서 아이디에 해당하는 센서의 채널들의 리스트를 반환합니다.
     * @param sensorId 리스트를 가져올 센서의 아이디입니다.
     * @return 조건에 해당하는 채널 리스트를 반환합니다.
     */
    @GetMapping("/{sensorId}/channels")
    public ResponseEntity<List<ChannelDto>> getChannels(@PathVariable("sensorId") int sensorId) {
        return ResponseEntity.ok(channelService.getChannelList(sensorId));
    }

    /**
     * 장소 별 채널 리스트를 반환하는 메서드입니다.
     * @param placeId 장소의 아이디
     * @return 채널 리스트
     */
    @GetMapping("/channels/by-place")
    public ResponseEntity<List<ChannelDto>> getChannelsFromPlace(@RequestParam int placeId) {
        return ResponseEntity.ok(channelService.getChannelListByPlace(placeId));
    }

    /**
     * sensorId에 해당하는 채널의 장소를 placeId로 변경합니다.
     * @param sensorId 장소를 변경할 sensor의 ID입니다.
     * @param placeId  센서의 장소를 가리키는 ID입니다.
     * @return 변경된 리스트의 개수를 반환합니다.
     */
    @PutMapping("/{sensorId}/place/{placeId}")
    @AdminOnly
    public ResponseEntity<Integer> updatePlace(@PathVariable("sensorId") int sensorId,
                                               @PathVariable("placeId") int placeId) {
        return ResponseEntity.ok(channelService.updatePlace(sensorId, placeId));
    }

    /**
     * 센서 아이디에 해당하는 센서의 채널명을 변경합니다.
     * @param channelDto sensorId와 변경할 이름의 channel-Name이 있는 리퀘스트 입니다.
     * @return 변경된 채널의 아이디를 반환합니다.
     */
    @PutMapping("/{sensorId}/channels/change-name")
    @AdminOnly
    public ResponseEntity<Integer> updateChannelName(@PathVariable("sensorId") int sensorId,
                                                     @RequestBody ChannelDto channelDto) {
        return ResponseEntity.ok(channelService.updateChannelName(sensorId, channelDto));
    }

}
