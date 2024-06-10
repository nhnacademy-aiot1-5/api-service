package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.DailyPredictedElectricity;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
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
    ElectricityResponseDto electricityResponseDto1;
    ElectricityResponseDto electricityResponseDto2;
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

        electricityResponseDto1 = new ElectricityResponseDto();
        electricityResponseDto2 = new ElectricityResponseDto();

        BeanUtils.copyProperties(predictedDailyElectricity1, electricityResponseDto1);
        BeanUtils.copyProperties(predictedDailyElectricity2, electricityResponseDto2);

        predictedDailyElectricityList = new ArrayList<>();
        predictedDailyElectricityList.add(predictedDailyElectricity1);
        predictedDailyElectricityList.add(predictedDailyElectricity2);

    }

    @Test
    void getCurrentMonthPredictions() {
        given(predictedElectricityRepository.findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(LocalDateTime.now(), LocalDateTime.now(), 1, -1)).willReturn(predictedDailyElectricityList);

        List<ElectricityResponseDto> list =  predictedElectricityService.getCurrentMonthPredictions(LocalDateTime.now(), 1);
        list.add(electricityResponseDto1);
        list.add(electricityResponseDto2);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0)).isEqualTo(electricityResponseDto1);
        assertThat(list.get(1)).isEqualTo(electricityResponseDto2);
    }

    @Test
    void getNextMonthPrediction() {
        given(predictedElectricityRepository.findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(LocalDateTime.now(), LocalDateTime.now(), 1, -1)).willReturn(predictedDailyElectricityList);

        List<ElectricityResponseDto> list =  predictedElectricityService.getNextMonthPrediction(LocalDateTime.now(), 1);
        list.add(electricityResponseDto1);
        list.add(electricityResponseDto2);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0)).isEqualTo(electricityResponseDto1);
        assertThat(list.get(1)).isEqualTo(electricityResponseDto2);
    }

    @Test
    void getThisMonthPrediction() {
        given(predictedElectricityRepository.findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(LocalDateTime.now(), LocalDateTime.now(), 1, -1)).willReturn(predictedDailyElectricityList);

        List<ElectricityResponseDto> list =  predictedElectricityService.getThisMonthPrediction(LocalDateTime.now(), 1);
        list.add(electricityResponseDto1);
        list.add(electricityResponseDto2);

        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0)).isEqualTo(electricityResponseDto1);
        assertThat(list.get(1)).isEqualTo(electricityResponseDto2);
    }
}