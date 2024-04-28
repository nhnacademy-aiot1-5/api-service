package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.dto.ChannelResponseDto;
import live.ioteatime.apiservice.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/channels")
public class ChannelController {
    private final ChannelService channelService;
    private final String X_USER_ID = "X-USER-ID";

    @PutMapping
    @AdminOnly
    public ResponseEntity<Integer> updatePlace(int sensorId, int placeId) {
        return ResponseEntity.ok(channelService.updatePlace(sensorId, placeId));
    }

    @GetMapping
    public ResponseEntity<List<Channel>> getChannels(int sensorId) {
        return ResponseEntity.ok(channelService.getChannelList(sensorId));
    }

    @GetMapping("/by_place")
    public ResponseEntity<List<ChannelResponseDto>> getChannelsFromPlace(@RequestParam int placeId) {
        List<Channel> channels = channelService.getChannelListByPlace(placeId);
        List<ChannelResponseDto> channelResponseDtos = new ArrayList<>();
        for (Channel c : channels) {
            channelResponseDtos.add(
                    new ChannelResponseDto(
                            c.getId(),
                            c.getSensor().getId(),
                            c.getPlace().getId(),
                            c.getChannelName()
                    )
            );
        }

        return ResponseEntity.ok(channelResponseDtos);
    }
}
