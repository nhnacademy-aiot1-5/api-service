package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.electricity.PreciseElectricityResponseDto;
import live.ioteatime.apiservice.service.impl.HourlyElectricityServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HourlyElectricityController.class)
class HourlyElectricityControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    HourlyElectricityServiceImpl hourlyElectricityService;

    @Test
    void getOneHourTotalElectricties() throws Exception {
        List<PreciseElectricityResponseDto> responseDtoList = generateResponse();
        given(hourlyElectricityService.getOneHourTotalElectricties(anyInt())).willReturn(responseDtoList);

        ResultActions result = mockMvc.perform(get("/hourly/electricities/total")
                .param("organizationId", "1"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(12)));
    }

    List<PreciseElectricityResponseDto> generateResponse() {
        LocalDateTime start = LocalDateTime.now();
        List<PreciseElectricityResponseDto> result = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            result.add(new PreciseElectricityResponseDto(start, 0.0));
            start = start.plusMinutes(5);
        }
        return result;
    }
}