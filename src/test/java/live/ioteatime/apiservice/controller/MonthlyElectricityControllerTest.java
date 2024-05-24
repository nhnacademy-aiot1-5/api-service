package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.electricity.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.service.ElectricityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MonthlyElectricityController.class)
class MonthlyElectricityControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean(name = "monthlyElectricityService")
    ElectricityService electricityService;
    LocalDateTime localDateTime;
    ElectricityResponseDto electricityResponseDto;

    @BeforeEach
    void setUp() {
        localDateTime = LocalDateTime.of(2024, Month.MARCH, 31, 0, 0, 0, 0);
        electricityResponseDto = new ElectricityResponseDto(localDateTime, 1000L, 1000L);
    }

    @Test
    @DisplayName("지정된 날짜와 채널 Id에 해당하는 월별 전력 사용량을 요청하는 테스트")
    void getMonthlyElectricity() throws Exception {
        // given
        given(electricityService.getElectricityByDate(
                new ElectricityRequestDto(
                        localDateTime,
                        1
                ))).willReturn(electricityResponseDto);
        // when
        ResultActions resultActions = mockMvc.perform(
                get("/monthly/electricity")
                        .param("localDateTime", String.valueOf(localDateTime))
                        .param("channelId", String.valueOf(1))
        );
        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time").value(localDateTime + ":00"))
                .andExpect(jsonPath("$.kwh").value(1000))
                .andExpect(jsonPath("$.bill").value(1000));
    }

    @Test
    @DisplayName("모든 메인 채널의  요청하는 테스트")
    void getLastMonthElectricity() throws Exception {
        //given
        given(electricityService.getLastElectricity())
                .willReturn(electricityResponseDto);
        //when
        ResultActions resultActions = mockMvc.perform(get("/monthly/electricity/last"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time").value(localDateTime + ":00"))
                .andExpect(jsonPath("$.kwh").value(1000))
                .andExpect(jsonPath("$.bill").value(1000));
    }

    @Test
    @DisplayName("이번 달 초 부터 현재 까지 사용한 월별 전력량 요청하는 테스트")
    void getcurrentMonthElectricity() throws Exception {
        //given
        given(electricityService.getCurrentElectricity())
                .willReturn(electricityResponseDto);
        //when
        ResultActions resultActions = mockMvc.perform(get("/monthly/electricity/current"));

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.time").value(localDateTime + ":00"))
                .andExpect(jsonPath("$.kwh").value(1000))
                .andExpect(jsonPath("$.bill").value(1000));
    }

    @Test
    @DisplayName("날짜와 채널 ID에 해당하는 모든 월별 전력량을 요청하는 테스트")
    void getMonthlyElectricties() throws Exception {
        //given
        given(electricityService.getElectricitiesByDate(
                new ElectricityRequestDto(
                        localDateTime,
                        1
                ))).willReturn(List.of(electricityResponseDto));
        //when
        ResultActions resultActions = mockMvc.perform(get("/monthly/electricities")
                .param("localDateTime", String.valueOf(localDateTime))
                .param("channelId", String.valueOf(1))
        );

        //then
        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time").value(localDateTime + ":00"))
                .andExpect(jsonPath("$[0].kwh").value(1000))
                .andExpect(jsonPath("$[0].bill").value(1000));
    }
}