package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.DailyPredictedElectricity;
import live.ioteatime.apiservice.dto.electricity.PreciseElectricityResponseDto;
import live.ioteatime.apiservice.repository.DailyPredictedElectricityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

class PredictedElectricityServiceTest {

    @Mock
    DailyPredictedElectricityRepository predictedElectricityRepository;

    @InjectMocks
    PredictedElectricityServiceImpl predictedElectricityService;

    DailyPredictedElectricity predictedDailyElectricity1;
    DailyPredictedElectricity predictedDailyElectricity2;
    PreciseElectricityResponseDto preciseElectricityResponseDto1;
    PreciseElectricityResponseDto preciseElectricityResponseDto2;
    List<DailyPredictedElectricity> predictedDailyElectricityList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        predictedDailyElectricity1 = new DailyPredictedElectricity();
        predictedDailyElectricity2 = new DailyPredictedElectricity();

        predictedDailyElectricity1.setTime(LocalDateTime.of(2020, 1, 1, 1, 1, 1));
        predictedDailyElectricity1.setKwh(12.3);
        predictedDailyElectricity2.setTime(LocalDateTime.of(2020, 1, 1, 2, 1, 1));
        predictedDailyElectricity2.setKwh(12.6);

        preciseElectricityResponseDto1 = new PreciseElectricityResponseDto();
        preciseElectricityResponseDto2 = new PreciseElectricityResponseDto();

        BeanUtils.copyProperties(predictedDailyElectricity1, preciseElectricityResponseDto1);
        BeanUtils.copyProperties(predictedDailyElectricity2, preciseElectricityResponseDto2);

        predictedDailyElectricityList = new ArrayList<>();
        predictedDailyElectricityList.add(predictedDailyElectricity1);
        predictedDailyElectricityList.add(predictedDailyElectricity2);

    }

    @Test
    void getCurrentMonthPredictions() {
        given(predictedElectricityRepository.findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(LocalDateTime.now(), LocalDateTime.now(), 1, -1)).willReturn(predictedDailyElectricityList);

        List<PreciseElectricityResponseDto> list =  predictedElectricityService.getCurrentMonthPredictions(LocalDateTime.now(), 1);
        list.add(preciseElectricityResponseDto1);
        list.add(preciseElectricityResponseDto2);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0)).isEqualTo(preciseElectricityResponseDto1);
        assertThat(list.get(1)).isEqualTo(preciseElectricityResponseDto2);
    }

    @Test
    void getNextMonthPrediction() {
        given(predictedElectricityRepository.findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(LocalDateTime.now(), LocalDateTime.now(), 1, -1)).willReturn(predictedDailyElectricityList);

        List<PreciseElectricityResponseDto> list =  predictedElectricityService.getNextMonthPrediction(LocalDateTime.now(), 1);
        list.add(preciseElectricityResponseDto1);
        list.add(preciseElectricityResponseDto2);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0)).isEqualTo(preciseElectricityResponseDto1);
        assertThat(list.get(1)).isEqualTo(preciseElectricityResponseDto2);
    }

    @Test
    void getThisMonthPrediction() {
        given(predictedElectricityRepository.findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(LocalDateTime.now(), LocalDateTime.now(), 1, -1)).willReturn(predictedDailyElectricityList);

        List<PreciseElectricityResponseDto> list =  predictedElectricityService.getThisMonthPrediction(LocalDateTime.now(), 1);
        list.add(preciseElectricityResponseDto1);
        list.add(preciseElectricityResponseDto2);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0)).isEqualTo(preciseElectricityResponseDto1);
        assertThat(list.get(1)).isEqualTo(preciseElectricityResponseDto2);
    }
}