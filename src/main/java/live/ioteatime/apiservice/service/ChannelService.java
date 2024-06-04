package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.channel.ChannelDto;

import java.util.List;

/**
 * 채널 서비스에 필요한 기능을 구현한 서비스 입니다.
 */
public interface ChannelService {

    /**
     * Controller의 getChannels에 사용되는 서비스로 센서 아이디에 소속된 채널의 리스트를 불러옵니다.
     * @param sensorId 리스트를 불러올 센서의 아이디입니다.
     * @return sensorId에 해당하는 ChannelDtoList를 반환합니다.
     */
    List<ChannelDto> getChannelList(int sensorId);

    /**
     * Controller의 getChannelsFromPlace에 사용되는 서비스로 장소에 해당하는 채널의 리스트를 불러옵니다.
     * @param placeId 리스트를 불러올 장소의 아이디입니다.
     * @return placdId에 해당하는 ChannelDtoList를 반환합니다.
     */
    List<ChannelDto> getChannelListByPlace(int placeId);

    /**
     * @param sensorId 센서아이디
     * @return 센서에 채널이 1개 이상 존재하면 true, 그렇지 않으면 false
     */
    boolean existChannelCheck(int sensorId);

    /**
     * 모드버스 채널을 추가합니다.
     * @param sensorId 채널이 추가될 센서의 아이디입니다.
     * @return 생성된 채널의 개수를 반환합니다.
     */
    int createChannel(int sensorId, ChannelDto channel);

    /**
     * Controller의 updateChannelPlace에 사용되는 서비스로 장소를 placeName으로 변경합니다.
     * @param channelId
     * @param placeName 바꿀 장소의 이름입니다.
     * @return
     */
    int updateChannelPlace(int channelId, String placeName);

    /**
     * 채널 정보를 수정하고, 룰엔진에 수정 요청을 전송합니다.
     * 채널 이름, Address, Type, Function-Code 만 수정 가능합니다.
     * @param channelId 채널아이디
     * @param channelDto 채널 정보 수정 요청
     * @return 채널아이디
     */
    int updateChannelInfo(int channelId, ChannelDto channelDto);

    /**
     * 채널을 삭제하고, 룰엔진에 삭제 요청을 전송합니다.
     * @param sensorId 센서아이디
     * @param channelId 채널아이디
     */
    void deleteChannel(int sensorId, int channelId);
}
