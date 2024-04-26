package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.annotation.AdminOnly;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
