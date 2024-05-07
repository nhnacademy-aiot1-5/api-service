package live.ioteatime.apiservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.apiservice.domain.Alive;
import live.ioteatime.apiservice.dto.place.PlaceResponseDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorRequest;
import live.ioteatime.apiservice.service.impl.MqttSensorServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MqttSensorController.class)
class MqttSensorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MqttSensorServiceImpl mqttSensorService;

    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void getSupportedMqttSensors() throws Exception {
        MqttSensorDto supportedMqttSensor = new MqttSensorDto();
        supportedMqttSensor.setId(1);
        supportedMqttSensor.setModelName("ABC123");

        Mockito.when(mqttSensorService.getAllSupportedSensors())
                .thenReturn(List.of(supportedMqttSensor));

        mockMvc.perform(get("/sensors/mqtt/supported")
                        .header("X-USER-ID", "admin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sensor_model_name").value("ABC123"));
    }

    @Test
    void getMqttSensors() throws Exception {
        MqttSensorDto sensor1 = new MqttSensorDto();
        MqttSensorDto sensor2 = new MqttSensorDto();

        Mockito.when(mqttSensorService.getOrganizationSensorsByUserId(anyString()))
                .thenReturn(List.of(sensor1, sensor2));

        mockMvc.perform(get("/sensors/mqtt/list")
                .header("X-USER-ID", "admin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getMqttSensor() throws Exception {
        MqttSensorDto testSensor = new MqttSensorDto();
        testSensor.setId(1);
        testSensor.setName("test_sensor");
        testSensor.setIp("0.0.0.0");
        testSensor.setPort("1880");
        testSensor.setAlive(Alive.DOWN);
        testSensor.setPlace(new PlaceResponseDto(1,"office"));

        Mockito.when(mqttSensorService.getSensorById(anyInt()))
                        .thenReturn(testSensor);

        mockMvc.perform(get("/sensors/mqtt/1")
                .header("X-USER-ID", "admin"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.sensor_name").value("test_sensor"))
                .andExpect(jsonPath("$.place.place_name").value("office"));
    }

    @Test
    void addMqttSensor() throws Exception {

        Mockito.when(mqttSensorService.addMqttSensor(anyString(), any()))
                        .thenReturn(1);

        MqttSensorRequest request = new MqttSensorRequest();
        request.setName("name");
        request.setModelName("model");
        request.setIp("0.0.0.0");
        request.setPort("1880");
        request.setPlaceId(1);
        request.setTopic("#");
        request.setDescription("");

        mockMvc.perform(post("/sensors/mqtt")
                .header("X-USER-ID","admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated());

    }

    @Test
    void updateMqttSensor() throws Exception {

        Mockito.when(mqttSensorService.updateMqttSensor(anyInt(),any()))
                .thenReturn(1);

        MqttSensorRequest request = new MqttSensorRequest();
        request.setName("change");
        request.setModelName("model");
        request.setIp("255.255.255.255");
        request.setPort("1880");
        request.setPlaceId(1);
        request.setTopic("#");
        request.setDescription("");

        mockMvc.perform(put("/sensors/mqtt/1")
                .header("X-USER-ID", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    void deleteMqttSensor() throws Exception {

        mockMvc.perform(delete("/sensors/mqtt/1")
                .header("X-USER-ID","admin"))
                .andDo(print())
                .andExpect(status().isNoContent());
    }
}