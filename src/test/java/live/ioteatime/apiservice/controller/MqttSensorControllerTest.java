package live.ioteatime.apiservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.apiservice.domain.Alive;
import live.ioteatime.apiservice.dto.place.PlaceDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorRequest;
import live.ioteatime.apiservice.service.MqttSensorService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MqttSensorController.class)
class MqttSensorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MqttSensorService mqttSensorService;

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MqttSensorDto supportedMqttSensor1;


    @Test
    @DisplayName("애플리케이션에서 지원하는 MQTT 센서 리스트를 가져오는 테스트")
    void getSupportedMqttSensors() throws Exception {
        supportedMqttSensor1 = new MqttSensorDto();
        supportedMqttSensor1.setId(1);
        supportedMqttSensor1.setModelName("ABC123");
        given(mqttSensorService.getAllSupportedSensors()).willReturn(List.of(supportedMqttSensor1));

        ResultActions resultActions = mockMvc.perform(get("/sensors/mqtt/supported")
                .header("X-USER-ID", "admin"));

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sensor_model_name").value("ABC123"));
    }

    @Test
    @DisplayName("유저가 소속한 조직의 등록된 모든 센서 리스트를 가져오는 테스트")
    void getMqttSensors() throws Exception {
        supportedMqttSensor1 = new MqttSensorDto();
        MqttSensorDto supportedMqttSensor2 = new MqttSensorDto();
        given(mqttSensorService.getOrganizationSensorsByUserId(anyString()))
                .willReturn(List.of(supportedMqttSensor1, supportedMqttSensor2));

        ResultActions resultActions = mockMvc.perform(get("/sensors/mqtt/list")
                .header("X-USER-ID", "admin"));

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @DisplayName("센서 아이디를 통해 mqtt 센서 정보를 단건 조회하는 테스트")
    void getMqttSensor() throws Exception {
        supportedMqttSensor1.setId(1);
        supportedMqttSensor1.setName("test_sensor");
        supportedMqttSensor1.setIp("0.0.0.0");
        supportedMqttSensor1.setPort("1880");
        supportedMqttSensor1.setAlive(Alive.DOWN);
        PlaceDto place = new PlaceDto();
        place.setId(1);
        place.setPlaceName("office");
        supportedMqttSensor1.setPlace(place);

        given(mqttSensorService.getSensorById(anyInt()))
                .willReturn(supportedMqttSensor1);

        ResultActions resultActions = mockMvc.perform(get("/sensors/mqtt/1")
                .header("X-USER-ID", "admin"));

        resultActions
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sensor_name").value("test_sensor"))
                .andExpect(jsonPath("$.place.place_name").value("office"));
    }

    @Test
    @DisplayName("mqtt 센서를 추가하는 테스트")
    void addMqttSensor() throws Exception {
        MqttSensorRequest request = new MqttSensorRequest();
        request.setName("name");
        request.setModelName("model");
        request.setIp("0.0.0.0");
        request.setPort("1880");
        request.setPlaceId(1);
        request.setTopic("#");
        request.setDescription("");
        given(mqttSensorService.addMqttSensor(anyString(), any()))
                .willReturn(1);

        ResultActions resultActions = mockMvc.perform(post("/sensors/mqtt")
                .header("X-USER-ID", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));

        resultActions
                .andExpect(status().isCreated());

    }

    @Test
    void updateMqttSensor() throws Exception {
        MqttSensorRequest request = new MqttSensorRequest();
        request.setName("change");
        request.setModelName("model");
        request.setIp("255.255.255.255");
        request.setPort("1880");
        request.setPlaceId(1);
        request.setTopic("#");
        request.setDescription("");
        given(mqttSensorService.updateMqttSensor(anyInt(), any()))
                .willReturn(1);

        ResultActions resultActions = mockMvc.perform(put("/sensors/mqtt/1")
                .header("X-USER-ID", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)));
        resultActions
                .andExpect(status().isOk());
    }

    @Test
    void deleteMqttSensor() throws Exception {

        mockMvc.perform(delete("/sensors/mqtt/1")
                        .header("X-USER-ID", "admin"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}