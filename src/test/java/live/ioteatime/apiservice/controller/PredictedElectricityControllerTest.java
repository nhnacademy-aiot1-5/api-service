package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.service.PredictedElectricityService;
import org.junit.jupiter.api.BeforeEach;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PredictedElectricityController.class)
class PredictedElectricityControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    PredictedElectricityService predictedElectricityService;

    ElectricityResponseDto response1;
    ElectricityResponseDto response2;
    ElectricityResponseDto response3;
    List<ElectricityResponseDto> responseList;

    @BeforeEach
    void setUp() {
        response1 = new ElectricityResponseDto();
        response2 = new ElectricityResponseDto();
        response3 = new ElectricityResponseDto();
        responseList = new ArrayList<>();

        response1.setTime(LocalDateTime.parse("2024-01-29T00:00:00"));
        response1.setKwh(1972.0);
        response2.setTime(LocalDateTime.parse("2024-01-30T00:00:00"));
        response2.setKwh(1972.11);
        response3.setTime(LocalDateTime.parse("2024-01-31T00:00:00"));
        response3.setKwh(1972.21);

        responseList.add(response1);
        responseList.add(response2);
        responseList.add(response3);
    }


    @Test
    void getMonthlyPredictedValues() throws Exception {
        given(predictedElectricityService.getCurrentMonthPredictions(any(), anyInt())).willReturn(responseList);

        ResultActions result = mockMvc.perform(get("/predicted")
                .param("requestTime", "2024-01-28T00:00:00")
                .param("organizationId", "1"));
        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].time").value("2024-01-29T00:00:00"))
                .andExpect(jsonPath("$[0].kwh").value(1972.0))
                .andExpect(jsonPath("$[1].time").value("2024-01-30T00:00:00"))
                .andExpect(jsonPath("$[1].kwh").value(1972.11))
                .andExpect(jsonPath("$[2].time").value("2024-01-31T00:00:00"))
                .andExpect(jsonPath("$[2].kwh").value(1972.21));
    }
}
