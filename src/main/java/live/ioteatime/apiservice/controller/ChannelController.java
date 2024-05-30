package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.annotation.VerifyOrganization;
import live.ioteatime.apiservice.dto.channel.ChannelDto;
import live.ioteatime.apiservice.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 채널과 관련된 요청을 처리하는 컨트롤러입니다.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/sensors/modbus")
@Tag(name="Channel", description = "Modbus 센서 채널 API")
public class ChannelController {
    private final ChannelService channelService;

    /**
     * 센서 아이디에 해당하는 센서의 채널들의 리스트를 반환합니다.
     *
     * @param sensorId 리스트를 가져올 센서의 아이디입니다.
     * @return 조건에 해당하는 채널 리스트를 반환합니다.
     */
    @GetMapping("/{sensorId}/channels")
    @VerifyOrganization
    @Operation(summary = "채널 리스트 조회", description = "해당 센서의 전체 채널 리스트를 조회합니다.")
    public ResponseEntity<List<ChannelDto>> getChannels(@PathVariable("sensorId") int sensorId) {
        return ResponseEntity.status(HttpStatus.OK).body(channelService.getChannelList(sensorId));
    }

    /**
     * 장소 별 채널 리스트를 반환하는 메서드입니다.
     *
     * @param placeId 장소의 아이디
     * @return 채널 리스트
     */
    @GetMapping("/channels/{placeId}")
    @Operation(summary = "장소 리스트 조회", description = "조직의 전체 장소 리스트를 조회합니다.")
    public ResponseEntity<List<ChannelDto>> getChannelsFromPlace(@PathVariable("placeId") int placeId) {
        List<ChannelDto> channelDto = channelService.getChannelListByPlace(placeId);
        return ResponseEntity.status(HttpStatus.OK).body(channelDto);
    }

    /**
     * 센서를 삭제하기 이전에 센서에 소속된 채널이 있는지 확인하는 메서드입니다.
     * @param sensorId 채널이 있는지 확인할 센서의 아이디입니다.
     * @return
     */
    @GetMapping("/{sensorId}/exist-channels")
    @AdminOnly
    @VerifyOrganization
    @Operation(summary = "채널 존재 여부 조회",
            description = "센서에 채널이 하나라도 존재하면 센서를 삭제할 수 없으므로, 해당 센서에 채널 존재 여부를 조회합니다.")
    public ResponseEntity<Boolean> existChannelCheck(@PathVariable("sensorId") int sensorId){
        return ResponseEntity.status(HttpStatus.OK).body(channelService.existChannelCheck(sensorId));
    }

    /**
     * 모드버스 센서에 새 채널을 추가하는 메서드입니다.
     * @param sensorId 센서아이디
     * @param channelDto 채널 추가 요청 데이터
     * @return 성공:200ok
     */
    @PostMapping("/{sensorId}/channels")
    @AdminOnly
    @VerifyOrganization
    @Operation(summary = "채널 등록", description = "Modbus 센서의 채널을 등록합니다.")
    public ResponseEntity<Integer> createChannel(@PathVariable("sensorId") int sensorId, @RequestBody ChannelDto channelDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(channelService.createChannel(sensorId, channelDto));
    }

    /**
     * sensorId에 해당하는 채널의 장소를 placeId로 변경합니다.
     *
     * @param channelId 장소를 변경할 채널의 아이디입니다.
     * @param channelPlace  변경할 장소의 값입니다.
     * @return 변경된 리스트의 개수를 반환합니다.
     */
    @PutMapping("/{channelId}/change-place")
    @AdminOnly
    @VerifyOrganization
    @Operation(summary = "채널 장소 수정", description = "채널의 장소를 수정합니다.")
    public ResponseEntity<Integer> updateChannelPlace(@PathVariable("channelId") int channelId, String channelPlace) {
        return ResponseEntity.status(HttpStatus.OK).body(channelService.updateChannelPlace(channelId, channelPlace));
    }

    /**
     * 센서 아이디에 해당하는 센서의 채널명을 변경합니다.
     * @param channelId 정보를 변경할 채널의 아이디입니다.
     * @param channelDto sensorId와 변경할 Address, Quantity, Function-Code가 있는 리퀘스트 입니다.
     * @return 변경된 채널의 센서 아이디를 반환합니다.
     */
    @AdminOnly
    @VerifyOrganization
    @PutMapping("/{channelId}/change-info")
    @Operation(summary = "채널 정보 수정", description = "채널의 Address, Quantity, Function-code를 수정합니다.")
    public ResponseEntity<Integer> updateChannelInfo(@PathVariable("channelId") int channelId, @RequestBody ChannelDto channelDto) {
        return ResponseEntity.status(HttpStatus.OK).body(channelService.updateChannelInfo(channelId, channelDto));
    }

    /**
     * 센서 아이디에 해당하는 센서를 삭제합니다.
     * @param sensorId 센서아이디
     * @param channelId 채널아이디
     * @return 204 No Content
     */
    @AdminOnly
    @VerifyOrganization
    @DeleteMapping("/{sensorId}/channels/{channelId}")
    @Operation(summary = "채널 삭제", description = "채널을 삭제합니다.")
    public ResponseEntity<String> deleteChannel(@PathVariable("sensorId") int sensorId, @PathVariable("channelId") int channelId) {
        channelService.deleteChannel(sensorId, channelId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
