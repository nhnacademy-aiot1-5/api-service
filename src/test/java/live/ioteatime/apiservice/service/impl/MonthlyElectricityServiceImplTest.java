package live.ioteatime.apiservice.service.impl;

import com.influxdb.client.InfluxDBClient;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.MonthlyElectricity;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.electricity.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.ChannelRepository;
import live.ioteatime.apiservice.repository.MonthlyElectricityRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MonthlyElectricityServiceImplTest {
    @Mock
    private MonthlyElectricityRepository monthlyElectricityRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private PlaceRepository placeRepository;
    @InjectMocks
    private MonthlyElectricityServiceImpl monthlyElectricityService;

    @Test
    void getElectricityByDate() {
        ElectricityRequestDto electricityRequestDto = new ElectricityRequestDto(
                LocalDateTime.of(2024, 1, 1, 0, 0),
                1
        );
        MonthlyElectricity.Pk pk = new MonthlyElectricity.Pk(
                electricityRequestDto.getChannelId(),
                electricityRequestDto.getTime()
        );
        MonthlyElectricity mockMonthlyElectricity = new MonthlyElectricity();
        mockMonthlyElectricity.setPk(pk);
        mockMonthlyElectricity.setKwh(100.0);
        mockMonthlyElectricity.setBill(50L);

        given(monthlyElectricityRepository.findMonthlyElectricityByPk(any())).willReturn(Optional.of(mockMonthlyElectricity));
        assertEquals(
                ElectricityResponseDto.class,
                monthlyElectricityService.getElectricityByDate(electricityRequestDto).getClass()
        );
        assertEquals(50L, monthlyElectricityService.getElectricityByDate(electricityRequestDto).getBill());
        assertEquals(100L, monthlyElectricityService.getElectricityByDate(electricityRequestDto).getKwh());

        given(monthlyElectricityRepository.findMonthlyElectricityByPk(any())).willReturn(Optional.empty());
        Assertions.assertThrows(ElectricityNotFoundException.class, () ->
                monthlyElectricityService.getElectricityByDate(electricityRequestDto));
    }

    @Test
    void getElectricitiesByDate() {
        ElectricityRequestDto electricityRequestDto = new ElectricityRequestDto(
                LocalDateTime.of(2024, 1, 1, 1, 1),
                1
        );
        MonthlyElectricity.Pk pk = new MonthlyElectricity.Pk(
                electricityRequestDto.getChannelId(),
                electricityRequestDto.getTime()
        );
        MonthlyElectricity mockMonthlyElectricity = new MonthlyElectricity();
        mockMonthlyElectricity.setPk(pk);
        mockMonthlyElectricity.setKwh(100.0);
        mockMonthlyElectricity.setBill(50L);

        given(monthlyElectricityRepository.findAllByPkChannelIdAndPkTimeBetween(
                any(Integer.class),
                any(LocalDateTime.class),
                any(LocalDateTime.class)))
                .willReturn(List.of(mockMonthlyElectricity));

        assertEquals(
                1,
                monthlyElectricityService.getElectricitiesByDate(electricityRequestDto).size()
        );
        assertEquals(
                ElectricityResponseDto.class,
                monthlyElectricityService.getElectricitiesByDate(electricityRequestDto).get(0).getClass()
        );
        assertEquals(
                100L,
                monthlyElectricityService.getElectricitiesByDate(electricityRequestDto).get(0).getKwh()
        );
        assertEquals(
                50L,
                monthlyElectricityService.getElectricitiesByDate(electricityRequestDto).get(0).getBill()
        );
    }

    @Test
    void getLastElectricity() {
        // Given
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1).toLocalDate().atStartOfDay();

        Channel mainChannel1 = new Channel();
        mainChannel1.setId(1);
        Channel mainChannel2 = new Channel();
        mainChannel2.setId(2);
        List<Channel> channels = Arrays.asList(mainChannel1, mainChannel2);

        MonthlyElectricity electricity1 = new MonthlyElectricity();
        electricity1.setPk(new MonthlyElectricity.Pk(1, lastMonth));
        electricity1.setKwh(150.0);
        electricity1.setBill(0L);
        MonthlyElectricity electricity2 = new MonthlyElectricity();
        electricity2.setPk(new MonthlyElectricity.Pk(2, lastMonth));
        electricity2.setKwh(180.0);
        electricity2.setBill(0L);

        when(channelRepository.findAllByChannelName("main")).thenReturn(channels);
        when(monthlyElectricityRepository.findMonthlyElectricityByPk(any()))
                .thenReturn(Optional.of(electricity1));
        when(monthlyElectricityRepository.findMonthlyElectricityByPk(any()))
                .thenReturn(Optional.of(electricity2));

        // When
        ElectricityResponseDto responseDto = monthlyElectricityService.getLastElectricity();

        // Then
        assertNotNull(responseDto);
        assertEquals(360L, responseDto.getKwh());
        assertEquals(0L, responseDto.getBill());
        assertEquals(lastMonth, responseDto.getTime());
    }

    @Test
    void getTotalElectricitiesByDate() {
        // Given
        LocalDateTime localDateTime = LocalDateTime.of(2023, 11, 15, 0, 0);
        int organizationId = 1;
        Organization organization = new Organization();
        organization.setId(1);
        organization.setName("");
        organization.setOrganizationCode("");
        organization.setElectricityBudget(0L);

        LocalDateTime start = localDateTime.withMonth(1).withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = localDateTime.minusMonths(1).withDayOfMonth(31)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        Place place1 = new Place(1, "Place1", organization);
        Place place2 = new Place(2, "Place2", organization);
        List<Place> placeList = Arrays.asList(place1, place2);

        Channel channel1 = new Channel();
        channel1.setId(1);
        Channel channel2 = new Channel();
        channel2.setId(2);

        MonthlyElectricity.Pk pk1 = new MonthlyElectricity.Pk(1, start);
        MonthlyElectricity.Pk pk2 = new MonthlyElectricity.Pk(2, start.plusDays(1));

        MonthlyElectricity monthlyElectricity1 = new MonthlyElectricity();
        monthlyElectricity1.setPk(pk1);
        monthlyElectricity1.setKwh(100.0);
        monthlyElectricity1.setBill(0L);
        MonthlyElectricity monthlyElectricity2 = new MonthlyElectricity();
        monthlyElectricity2.setPk(pk2);
        monthlyElectricity2.setKwh(200.0);
        monthlyElectricity2.setBill(0L);

        List<MonthlyElectricity> electricityList1 = List.of(monthlyElectricity1);
        List<MonthlyElectricity> electricityList2 = List.of(monthlyElectricity2);

        when(placeRepository.findAllByOrganization_Id(organizationId)).thenReturn(placeList);
        when(channelRepository.findByPlaceAndChannelName(place1, "main")).thenReturn(channel1);
        when(channelRepository.findByPlaceAndChannelName(place2, "main")).thenReturn(channel2);
        when(monthlyElectricityRepository.findAllByPkChannelIdAndPkTimeBetween(channel1.getId(), start, end))
                .thenReturn(electricityList1);
        when(monthlyElectricityRepository.findAllByPkChannelIdAndPkTimeBetween(channel2.getId(), start, end))
                .thenReturn(electricityList2);

        // When
        List<ElectricityResponseDto> responseDtoList = monthlyElectricityService.getTotalElectricitiesByDate(localDateTime, organizationId);

        // Then
        assertNotNull(responseDtoList);
        assertEquals(304, responseDtoList.size());
    }
}