package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.dto.ChannelDto;
import live.ioteatime.apiservice.dto.UpdateChannelNameRequest;
import live.ioteatime.apiservice.dto.UpdatePlaceRequest;
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
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;
    private final String X_USER_ID = "X-USER-ID";

    /**
     * 해당하는 modbus센서의 장소를 해당하는 장소의 아이디로 변경합니다.
     * @param updatePlaceRequest placeId와 Modbus센서의 sensorId가 있는 리퀘스트 입니다.
     * @return 변경된 리스트의 숫자를 반환합니다.
     */
    @PutMapping
    @AdminOnly
    public ResponseEntity<Integer> updatePlace(@RequestBody UpdatePlaceRequest updatePlaceRequest) {
        return ResponseEntity.ok(channelService.updatePlace(updatePlaceRequest));
    }

    /**
     * 센서 아이디에 해당하는 센서의 채널들의 리스트를 반환합니다.
     * @param sensorId 리스트를 가져올 센서의 아이디입니다.
     * @return 조건에 해당하는 채널 리스트를 반환합니다.
     */
    @GetMapping
    public ResponseEntity<List<ChannelDto>> getChannels(int sensorId) {
        return ResponseEntity.ok(channelService.getChannelList(sensorId));
    }

    /**
     * 센서 아이디에 해당하는 센서의 이름을 변경합니다.
     * @param updateChannelNameRequest sensorId와 변경할 이름의 channelName이 있는 리퀘스트 입니다.
     * @return 변경된 채널의 아이디를 반환합니다.
     */
    @PutMapping("/changename")
    @AdminOnly
    public ResponseEntity<Integer> updateChannelName(@RequestBody UpdateChannelNameRequest updateChannelNameRequest){
        return ResponseEntity.ok(channelService.updateChannelName(updateChannelNameRequest));
    }

}
