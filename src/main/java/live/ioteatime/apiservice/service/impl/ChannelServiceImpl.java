package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.ModbusSensor;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.*;
import live.ioteatime.apiservice.exception.ChannelNotFoundException;
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
            channel.setChannelName(sensor.getName() + "_" + i);
            ChannelDto channelDto = new ChannelDto();
            BeanUtils.copyProperties(channel, channelDto);
            channelList.add(channelDto);
            channelRepository.save(channel);
        }
        return channelList.size();
    }

    @Override
    public int updatePlace(UpdatePlaceRequest updatePlaceRequest) {
        Place place = placeRepository.findById(updatePlaceRequest.getPlaceId()).orElseThrow(PlaceNotFoundException::new);
        List<Channel> channelList = channelRepository.findAllBySensor_Id(updatePlaceRequest.getSensorId());
        for (int i = 0; i < channelList.size(); i++) {
            channelList.get(i).setPlace(place);
            channelRepository.save(channelList.get(i));
        }
        return channelList.size();
    }

    @Override
    public List<ChannelDto> getChannelList(int sensorId) {
        List<Channel> channelList = channelRepository.findAllBySensor_Id(sensorId);
        List<ChannelDto> channelDtoList = new ArrayList<>();
        for (Channel channel : channelList) {
            ChannelDto channelDto = new ChannelDto();
            BeanUtils.copyProperties(channel, channelDto);

            ModbusSensorDto sensorDto = new ModbusSensorDto();
            BeanUtils.copyProperties(channel.getSensor(), sensorDto);
            channelDto.setSensor(sensorDto);

            PlaceWithoutOrganizationDto placeDto = new PlaceWithoutOrganizationDto();
            BeanUtils.copyProperties(channel.getPlace(), placeDto);
            channelDto.setPlace(placeDto);

            channelDtoList.add(channelDto);
        }
        return channelDtoList;
    }

    @Override
    public int updateChannelName(UpdateChannelNameRequest updateChannelNameRequest) {
        Channel channel = channelRepository.findById(updateChannelNameRequest.getId()).orElseThrow(ChannelNotFoundException::new);
        channel.setChannelName(updateChannelNameRequest.getChannelName());
        channelRepository.save(channel);
        return channel.getId();
    }

    @Override
    public List<Channel> getChannelListByPlace(int placeId) {
        return channelRepository.findAllByPlace_Id(placeId);
    }
}
