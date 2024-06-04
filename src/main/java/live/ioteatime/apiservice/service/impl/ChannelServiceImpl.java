package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.adaptor.ModbusSensorAdaptor;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.ModbusSensor;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.AddModbusSensorRequest;
import live.ioteatime.apiservice.dto.channel.ChannelDto;
import live.ioteatime.apiservice.dto.place.PlaceDto;
import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
import live.ioteatime.apiservice.exception.ChannelNotFoundException;
import live.ioteatime.apiservice.exception.SensorNotFoundException;
import live.ioteatime.apiservice.repository.ChannelRepository;
import live.ioteatime.apiservice.repository.ModbusSensorRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.ChannelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Slf4j
@Service
@RequiredArgsConstructor
public class ChannelServiceImpl implements ChannelService {

    private final ChannelRepository channelRepository;
    private final ModbusSensorRepository modbusSensorRepository;
    private final PlaceRepository placeRepository;
    private final ModbusSensorAdaptor modbusSensorAdaptor;

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

            if (channel.getPlace() != null) {
                PlaceDto placeDto = new PlaceDto();
                BeanUtils.copyProperties(channel.getPlace(), placeDto);
                channelDto.setPlace(placeDto);
            }
            channelDtoList.add(channelDto);
        }
        return channelDtoList;
    }

    @Override
    public List<ChannelDto> getChannelListByPlace(int placeId) {
        List<Channel> channelList = channelRepository.findAllByPlace_Id(placeId);
        List<ChannelDto> channelDtoList = new ArrayList<>();
        for (Channel channel : channelList) {
            ChannelDto channelDto = new ChannelDto();
            BeanUtils.copyProperties(channel, channelDto);

            PlaceDto placeDto = new PlaceDto();
            BeanUtils.copyProperties(channel.getPlace(), placeDto);
            channelDto.setPlace(placeDto);

            ModbusSensorDto sensorDto = new ModbusSensorDto();
            BeanUtils.copyProperties(channel.getSensor(), sensorDto);
            channelDto.setSensor(sensorDto);

            channelDtoList.add(channelDto);
        }
        return channelDtoList;
    }

    @Override
    public boolean existChannelCheck(int sensorId) {
        return channelRepository.existsBySensor_Id(sensorId);
    }

    @Override
    public int createChannel(int sensorId, ChannelDto channelDto) {
        ModbusSensor sensor = modbusSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        ModbusSensorDto sensorDto = new ModbusSensorDto();
        BeanUtils.copyProperties(sensor, sensorDto);
        channelDto.setSensor(sensorDto);

        Place place = placeRepository.findByPlaceName(channelDto.getPlaceName());
        PlaceDto placeDto = new PlaceDto();
        BeanUtils.copyProperties(place, placeDto);
        channelDto.setPlace(placeDto);

        Channel channel = new Channel();
        BeanUtils.copyProperties(channelDto, channel);
        channel.setSensor(sensor);
        channel.setPlace(place);

        channelRepository.save(channel);

        int channelCount = channelRepository.countBySensor_Id(sensorId);
        sensor.setChannelCount(channelCount);
        modbusSensorRepository.save(sensor);

        sensor = modbusSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        AddModbusSensorRequest modbusSensorRequest = new AddModbusSensorRequest();
        modbusSensorRequest.setName(sensor.getSensorName());
        modbusSensorRequest.setHost(sensor.getIp());

        StringBuilder channels = new StringBuilder();
        channelRepository.findAllBySensor_Id(sensor.getId())
                .forEach(c -> channels.append(c.getFunctionCode() + "/" + c.getAddress() + "/" + c.getType() + ", "));
        int length = channels.length();
        channels.delete(length - 2, length);
        modbusSensorRequest.setChannel(channels.toString());
        log.info("Send request to Rule Engine: URL=/modbus, method=POST, body=\"{}\"", modbusSensorRequest.getChannel());

        modbusSensorAdaptor.addModbusSensor(modbusSensorRequest);
        modbusSensorAdaptor.getUpdateCheck();

        return sensorId;
    }

    @Override
    public int updateChannelPlace(int channelId, String placeName) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(ChannelNotFoundException::new);
        Place place = placeRepository.findByPlaceName(placeName);
        channel.setPlace(place);
        channelRepository.save(channel);
        return channel.getSensor().getId();
    }

    @Override
    public int updateChannelInfo(int channelId, ChannelDto channelDto) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(ChannelNotFoundException::new);
        channel.setChannelName(channelDto.getChannelName());
        channel.setAddress(channelDto.getAddress());
        channel.setType(channelDto.getType());
        channel.setFunctionCode(channelDto.getFunctionCode());

        channelRepository.save(channel);
        modbusSensorAdaptor.getUpdateCheck();

        return channel.getSensor().getId();
    }

    @Override
    public void deleteChannel(int sensorId, int channelId) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(ChannelNotFoundException::new);
        channelRepository.delete(channel);


        ModbusSensor sensor = modbusSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);
        int channelCount = channelRepository.countBySensor_Id(sensorId);
        sensor.setChannelCount(channelCount);

        modbusSensorRepository.save(sensor);

        sensor = modbusSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        log.info("Send request to Rule Engine: URL=/modbus, method=get, body=\"{}\"", sensor.getSensorName());

        modbusSensorAdaptor.deleteModbusSensor(sensor.getSensorName());
        modbusSensorAdaptor.getUpdateCheck();
    }
}
