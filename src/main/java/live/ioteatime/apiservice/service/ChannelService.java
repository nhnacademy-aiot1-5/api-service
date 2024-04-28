package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Channel;

import java.util.List;

public interface ChannelService {
    int createChannel(int sensorId);

    int updatePlace(int sensorId, int placeId);

    List<Channel> getChannelList(int sensorId);

    List<Channel> getChannelListByPlace(int placeId);
}
