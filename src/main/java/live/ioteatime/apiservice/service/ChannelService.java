package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.channel.ChannelDto;

import java.util.List;

public interface ChannelService {
    List<ChannelDto> getChannelList(int sensorId);

    List<ChannelDto> getChannelListByPlace(int placeId);

    boolean existChannelCheck(int sensorId);

    int createChannel(int sensorId, ChannelDto channel);

    int updateChannelPlace(int channelId, String channelPlace);

    int updateChannelName(int channelId, String channelName);

    int updateChannelInfo(int channelId, ChannelDto channelDto);

    void deleteChannel(int sensorId, int channelId);


}
