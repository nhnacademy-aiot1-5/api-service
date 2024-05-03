package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.ChannelDto;
import live.ioteatime.apiservice.dto.ChannelResponseDto;
import live.ioteatime.apiservice.dto.UpdateChannelNameRequest;
import live.ioteatime.apiservice.dto.UpdatePlaceRequest;

import java.util.List;

public interface ChannelService {
    List<ChannelDto> getChannelList(int sensorId);

    List<ChannelResponseDto>getChannelListByPlace(int placeId);

    int createChannel(int sensorId);

    int updatePlace(UpdatePlaceRequest updatePlaceRequest);

    int updateChannelName(UpdateChannelNameRequest updateChannelNameRequest);
}
