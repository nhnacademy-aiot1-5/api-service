package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.electricity.RealtimeElectricityResponseDto;
import live.ioteatime.apiservice.service.RealtimeElectricityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(RealtimeElectricityController.class)
class RealtimeElectricityControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    RealtimeElectricityService realtimeElectricityService;

    RealtimeElectricityResponseDto response1;
    RealtimeElectricityResponseDto response2;
    RealtimeElectricityResponseDto response3;
    List<RealtimeElectricityResponseDto> responseList;

    @BeforeEach
    void setUp() {
        response1 = new RealtimeElectricityResponseDto();
        response2 = new RealtimeElectricityResponseDto();
        response3 = new RealtimeElectricityResponseDto();
        responseList = new ArrayList<>();

        response1.setPlace("office");
        response1.setChannel("ac");
        response1.setW(200.4);

        response2.setPlace("office");
        response2.setChannel("ac");
        response2.setW(210.4);

        response3.setPlace("office");
        response3.setChannel("ac");
        response3.setW(222.4);

        responseList.add(response1);
        responseList.add(response2);
        responseList.add(response3);
    }

    @Test
    void getRealtimeElectricity() throws Exception {
        given(realtimeElectricityService.getRealtimeElectricity(anyInt())).willReturn(responseList);

        ResultActions result = mockMvc.perform(get("/realtime/electricity").param("organizationId", "1"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].place").value("office"))
                .andExpect(jsonPath("$[0].channel").value("ac"))
                .andExpect(jsonPath("$[0].w").value(200.4))
                .andExpect(jsonPath("$[1].w").value(210.4))
                .andExpect(jsonPath("$[2].w").value(222.4));
    }
}