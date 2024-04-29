package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.dto.ChannelDto;
import live.ioteatime.apiservice.dto.UpdateChannelNameRequest;
import live.ioteatime.apiservice.dto.UpdatePlaceRequest;

import java.util.List;

public interface ChannelService {
    int createChannel(int sensorId);

    int updatePlace(UpdatePlaceRequest updatePlaceRequest);

    List<ChannelDto> getChannelList(int sensorId);

    int updateChannelName(UpdateChannelNameRequest updateChannelNameRequest);
}
