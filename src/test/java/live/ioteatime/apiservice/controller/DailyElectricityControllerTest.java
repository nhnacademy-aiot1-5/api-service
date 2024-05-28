package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.service.ElectricityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(DailyElectricityController.class)
class DailyElectricityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    @Qualifier("dailyElectricityService")
    ElectricityService electricityService;

    ElectricityResponseDto electricityResponseDto;

    @BeforeEach
    void setUp() {
        electricityResponseDto = new ElectricityResponseDto(
                LocalDateTime.of(2024,5,27,0,0,0,0),
                123.0, 50_000L);
    }

    @Test
    void getDailyElectricities() throws Exception {
        given(electricityService.getElectricitiesByDate(any())).willReturn(List.of(electricityResponseDto));

        ResultActions result = mockMvc.perform(get("/daily/electricities")
                .param("localDateTime", "2024-05-27T00:00:00")
                .param("channelId", "1"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getcurrentMonthTotalElectricities() throws Exception {
        given(electricityService.getTotalElectricitiesByDate(any(), anyInt())).willReturn(List.of(electricityResponseDto));

        ResultActions result = mockMvc.perform(get("/daily/electricities/total")
                .param("localDateTime", "2024-05-27T00:00:00")
                .param("organizationId", "1"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getLastDayElectricity() throws Exception {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        ElectricityResponseDto response = new ElectricityResponseDto(today.minusDays(1), 100.0, 1000L);
        given(electricityService.getLastElectricity()).willReturn(response);

        ResultActions result = mockMvc.perform(get("/daily/electricity/last"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.kwh").value(100L));
    }

    @Test
    void getCurrentDayElectricity() throws Exception {
        LocalDateTime today = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        ElectricityResponseDto response = new ElectricityResponseDto(today, 100.0, 1000L);
        given(electricityService.getCurrentElectricity()).willReturn(response);

        ResultActions result = mockMvc.perform(get("/daily/electricity/current"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.kwh").value(100L));
    }
}