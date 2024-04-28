package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.ModbusSensor;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.ChannelDto;
import live.ioteatime.apiservice.exception.PlaceNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.repository.ChannelRepository;
import live.ioteatime.apiservice.repository.ModbusSensorRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {
    private final ChannelRepository channelRepository;
    private final ModbusSensorRepository modbusSensorRepository;
    private final PlaceRepository placeRepository;

    @Override
    public int createChannel(int sensorId) {
        ModbusSensor sensor = modbusSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        List<ChannelDto> channelList = new ArrayList<>();
        for (int i = 1; i <= sensor.getChannelCount(); i++) {
            Channel channel = new Channel();
            channel.setSensor(sensor);
            channel.setChannelName(sensor.getName() + i);
            ChannelDto channelDto = new ChannelDto();
            BeanUtils.copyProperties(channel, channelDto);
            channelList.add(channelDto);
            channelRepository.save(channel);
        }
        return channelList.size();
    }

    @Override
    public int updatePlace(int sensorId, int placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
        List<Channel> channelList = channelRepository.findALLBySensor_Id(sensorId);
        for (int i = 0; i < channelList.size(); i++) {
            channelList.get(i).setPlace(place);
            channelRepository.save(channelList.get(i));
        }
        return channelList.size();
    }

    @Override
    public List<Channel> getChannelList(int sensorId) {
        return channelRepository.findALLBySensor_Id(sensorId);
    }

    @Override
    public List<Channel> getChannelListByPlace(int placeId) {
        return channelRepository.findAllByPlace_Id(placeId);
    }
}
