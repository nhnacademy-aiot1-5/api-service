package live.ioteatime.apiservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
import live.ioteatime.apiservice.dto.sensor.SensorRequest;
import live.ioteatime.apiservice.service.ModbusSensorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ModbusSensorController.class)
class ModbusSensorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ModbusSensorService modbusSensorService;
    @Autowired
    ObjectMapper objectMapper;

    @Test
    @DisplayName("등록 가능한 Modbus 센서 리스트를 조회하는 테스트")
    void getSupportedModbusSensors() throws Exception {
        //given
        given(modbusSensorService.getAllSupportedSensors()).willReturn(List.of(new ModbusSensorDto()));
        //when
        ResultActions resultActions = mockMvc.perform(get("/sensors/modbus/supported"));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("모든 modbus 센서들의 리스트를 가져오는 API 테스트")
    void getModbusSensors() throws Exception {
        //given
        String userId = "asdf";
        given(modbusSensorService.getOrganizationSensorsByUserId(userId)).willReturn(List.of(new ModbusSensorDto()));
        //when
        ResultActions resultActions = mockMvc.perform(get("/sensors/modbus/list").header("X-USER-ID", userId));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @DisplayName("Modbus 센서 단일 조회 API 테스트")
    void getModbusSensor() throws Exception {
        //given
        int sensorId = 1;
        ModbusSensorDto modbusSensorDto = new ModbusSensorDto();
        modbusSensorDto.setId(sensorId);
        given(modbusSensorService.getSensorById(sensorId)).willReturn(modbusSensorDto);
        //when
        ResultActions resultActions = mockMvc.perform(get("/sensors/modbus/" + sensorId));
        //then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.sensor_id").value(1));
    }

    @Test
    @DisplayName("모드버스 센서를 추가하는 서비스로 센서의 정보를 통해서 센서를 추가하는 테스트")
    void addModbusSensor() throws Exception {
        //given
        String userId = "asdf";
        SensorRequest sensorRequest = new SensorRequest();
        given(modbusSensorService.addModbusSensor(userId, sensorRequest)).willReturn(1);
        //when
        String requestBody = objectMapper.writeValueAsString(sensorRequest);
        ResultActions resultActions = mockMvc.perform(post("/sensors/modbus")
                .header("X-USER-ID", userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(content().string(containsString("Sensor registered")));
    }

    @Test
    @DisplayName("Modbus 센서 정보를 수정하는 테스트")
    void updateModbusSensor() throws Exception {
        //given
        int sensorId = 1;
        SensorRequest sensorRequest = new SensorRequest();
        given(modbusSensorService.updateModbusSensor(eq(sensorId), any(SensorRequest.class))).willReturn(1);
        //when
        String requestBody = objectMapper.writeValueAsString(sensorRequest);
        ResultActions resultActions = mockMvc.perform(put("/sensors/modbus/" + sensorId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Sensor updated. id=" + sensorId)));
    }

    @Test
    @DisplayName("센서의 작동상태를 수정하는 요청 테스트")
    void updateHealth() throws Exception {
        //given
        int sensorId = 1;
        given(modbusSensorService.updateHealth(sensorId)).willReturn(1);
        //when
        ResultActions resultActions = mockMvc.perform(put("/sensors/modbus/health")
                .param("sensorId", String.valueOf(sensorId))
        );
        //then
        resultActions.andExpect(status().isOk())
                .andDo(print())
                .andExpect(content().string(containsString("Sensor health changed" + sensorId)));
    }

    @Test
    @DisplayName("Modbus 센서를 데이터베이스에서 삭제 요청 테스트")
    void deleteModbusSensor() throws Exception {
        //given
        int sensorId = 1;
        //when
        ResultActions resultActions = mockMvc.perform(delete("/sensors/modbus/" + sensorId));
        //then
        resultActions.andExpect(status().isNoContent());
    }
}