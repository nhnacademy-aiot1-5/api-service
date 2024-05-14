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

    /**
     * Controller의 getChannels에 사용되는 서비스로 센서 아이디에 소속된 채널의 리스트를 불러옵니다.
     * @param sensorId 리스트를 불러올 센서의 아이디입니다.
     * @return sensorId에 해당하는 ChannelDtoList를 반환합니다.
     */
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

    /**
     * Controller의 getChannelsFromPlace에 사용되는 서비스로 장소에 해당하는 채널의 리스트를 불러옵니다.
     * @param placeId 리스트를 불러올 장소의 아이디입니다.
     * @return placdId에 해당하는 ChannelDtoList를 반환합니다.
     */
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

    /**
     * 모드버스 채널을 추가합니다.
     * @param sensorId 채널이 추가될 센서의 아이디입니다.
     * @return 생성된 채널의 개수를 반환합니다.
     */
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

        sendRequestToRuleEngine(sensor.getId());

        return sensorId;
    }

    /**
     * Controller의 updateChannelPlace에 사용되는 서비스로 장소를 placeName으로 변경합니다.
     * @param channelId
     * @param placeName 바꿀 장소의 이름입니다.
     * @return
     */
    @Override
    public int updateChannelPlace(int channelId, String placeName) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(ChannelNotFoundException::new);
        Place place = placeRepository.findByPlaceName(placeName);
        channel.setPlace(place);
        channelRepository.save(channel);
        return channel.getSensor().getId();
    }

    /**
     * Controller의 updateChannelName에 사용되는 서비스로 ChannelDto에 담겨있는 ChannelName으로 변경합니다.
     * @param channelId
     * @param channelName 바꿀 채널의 아이디입니다.
     * @return 채널의 센서 ID를 반환합니다.
     */
    @Override
    public int updateChannelName(int channelId, String channelName) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(ChannelNotFoundException::new);
        channel.setChannelName(channelName);

        channelRepository.save(channel);
        sendRequestToRuleEngine(channel.getSensor().getId());

        return channel.getSensor().getId();
    }

    @Override
    public int updateChannelInfo(int channelId, ChannelDto channelDto) {
        Channel channel = channelRepository.findById(channelId).orElseThrow(ChannelNotFoundException::new);
        channel.setAddress(channelDto.getAddress());
        channel.setType(channelDto.getType());
        channel.setFunctionCode(channelDto.getFunctionCode());

        channelRepository.save(channel);
        sendRequestToRuleEngine(channel.getSensor().getId());

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
        sendRequestToRuleEngine(sensorId);
    }

    private void sendRequestToRuleEngine(int sensorId) {
        ModbusSensor sensor = modbusSensorRepository.findById(sensorId).orElseThrow(SensorNotFoundException::new);

        AddModbusSensorRequest modbusSensorRequest = new AddModbusSensorRequest();
        modbusSensorRequest.setName(sensor.getSensorName());
        modbusSensorRequest.setHost(sensor.getIp());

        StringBuilder channels = new StringBuilder();
        channelRepository.findAllBySensor_Id(sensor.getId())
                .forEach(c -> channels.append(c.getFunctionCode() + "/" + c.getAddress() + "/" + c.getType() + ","));
        int length = channels.length();
        channels.delete(length-2, length);
        modbusSensorRequest.setChannel(channels.toString());
        log.debug("Send request to Rule Engine: URL=/mqtt, method=POST, body=\"{}\"", modbusSensorRequest.getChannel());

        modbusSensorAdaptor.addModbusSensor(modbusSensorRequest);
    }
}
