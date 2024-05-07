package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.channel.ChannelDto;

import java.util.List;

public interface ChannelService {
    List<ChannelDto> getChannelList(int sensorId);

    List<ChannelDto>getChannelListByPlace(int placeId);

    int createChannel(int sensorId);

    int updatePlace(int sensorId, int placeId);

    int updateChannelName(int sensorId, String channelName);
}
