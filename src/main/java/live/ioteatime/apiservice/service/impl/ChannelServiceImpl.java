package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.ModbusSensor;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
import live.ioteatime.apiservice.dto.place.PlaceWithoutOrganizationDto;
import live.ioteatime.apiservice.dto.channel.ChannelDto;
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

            PlaceWithoutOrganizationDto placeDto = new PlaceWithoutOrganizationDto();
            BeanUtils.copyProperties(channel.getPlace(), placeDto);
            channelDto.setPlace(placeDto);

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

            PlaceWithoutOrganizationDto placeDto = new PlaceWithoutOrganizationDto();
            BeanUtils.copyProperties(channel.getPlace(), placeDto);
            channelDto.setPlace(placeDto);

            ModbusSensorDto sensorDto = new ModbusSensorDto();
            BeanUtils.copyProperties(channel.getSensor(), sensorDto);
            channelDto.setSensor(sensorDto);

            channelDtoList.add(channelDto);
        }
        return channelDtoList;
    }

    /**
     * 이 메서드는 ChannelController에 소속된 서비스가 아닌 ModbusSensorController에 소속된 컨트롤러로
     * ModbusSensor를 추가할 때 채널의 숫자의 개수만큼 새로운 행을 만듭니다.
     * @param sensorId 채널이 추가될 센서의 아이디입니다.
     * @return 생성된 채널의 개수를 반환합니다.
     */
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

    /**
     * Controller의 updatePlace에 사용되는 서비스로 modbus센서의 장소를 UpdatePlaceRequest의 placeId값으로 수정합니다.
     * @param sensorId 장소를 바꿀 sensor의 ID입니다.
     * @param placeId  바꿀 장소의 ID입니다.
     * @return 변경된 리스트의 개수를 반환합니다.
     */
    @Override
    public int updatePlace(int sensorId, int placeId) {
        Place place = placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
        List<Channel> channelList = channelRepository.findAllBySensor_Id(sensorId);
        for (Channel channel : channelList) {
            channel.setPlace(place);
            channelRepository.save(channel);
        }
        return channelList.size();
    }

    /**
     * Controller의 updateChannelName에 사용되는 서비스로 ChannelDto에 담겨있는 ChannelName으로 변경합니다.
     * @param sensorId   url 구성에 필수요소이나 해당 서비스에 필요하지 않아 예의상 받았습니다.
     * @param channelDto 변경할 channelName이 담겨있는 DTO 입니다.
     * @return
     */
    @Override
    public int updateChannelName(int sensorId, ChannelDto channelDto) {
        Channel channel = channelRepository.findById(channelDto.getId()).orElseThrow(ChannelNotFoundException::new);
        channel.setChannelName(channelDto.getChannelName());
        channelRepository.save(channel);
        return channel.getId();
    }
}
