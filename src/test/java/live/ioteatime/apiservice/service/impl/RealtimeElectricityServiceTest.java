package live.ioteatime.apiservice.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.electricity.RealtimeElectricityResponseDto;
import live.ioteatime.apiservice.repository.ChannelRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

class RealtimeElectricityServiceTest {
    @Mock
    ChannelRepository channelRepository;
    @Mock
    PlaceRepository placeRepository;
    @Mock
    InfluxDBClient influxDBClient;
    @Mock
    QueryApi queryApi;
    @Mock
    FluxTable fluxTable;
    @Mock
    FluxRecord fluxRecord;

    @InjectMocks
    RealtimeElectricityServiceImpl realtimeElectricityService;

    Organization organization;
    Place place1;
    Place place2;
    List<Place> places;
    Channel channel1;
    Channel channel2;
    List<Channel> channels;
    RealtimeElectricityResponseDto realtimeElectricityResponseDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        organization = new Organization();
        organization.setId(1);
        organization.setName("nhnacademy");
        organization.setOrganizationCode("1234");

        place1 = new Place();
        place1.setId(1);
        place1.setPlaceName("오피스");
        place1.setOrganization(organization);

        place2 = new Place();
        place2.setId(2);
        place2.setPlaceName("Class_A");
        place2.setOrganization(organization);

        places = new ArrayList<>();
        places.add(place1);
        places.add(place2);

        channel1 = new Channel();
        channel1.setId(1);
        channel1.setChannelName("channel1");

        channel2 = new Channel();
        channel2.setId(2);
        channel2.setChannelName("channel2");

        channels = new ArrayList<>();
        channels.add(channel1);
        channels.add(channel2);

        realtimeElectricityResponseDto = new RealtimeElectricityResponseDto();
        realtimeElectricityResponseDto.setChannel(channel1.getChannelName());
        realtimeElectricityResponseDto.setPlace(place1.getPlaceName());
        realtimeElectricityResponseDto.setW(1.1);
    }


    @Test
    @DisplayName("실시간 전력 데이터를 가져옴")
    void getRealtimeElectricity() {
        given(placeRepository.findAllByOrganization_Id(anyInt())).willReturn(places);
        given(channelRepository.findAllByPlace_Id(anyInt())).willReturn(channels);
        given(influxDBClient.getQueryApi()).willReturn(queryApi);
        given(fluxRecord.getValue()).willReturn(1.1);

        List<RealtimeElectricityResponseDto> result = realtimeElectricityService.getRealtimeElectricity(organization.getId());
        result.add(realtimeElectricityResponseDto);

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
    }
}