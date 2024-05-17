package live.ioteatime.apiservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.apiservice.domain.*;
import live.ioteatime.apiservice.dto.OrganizationDto;
import live.ioteatime.apiservice.dto.sensor.MqttSensorDto;
import live.ioteatime.apiservice.dto.topic.TopicDto;
import live.ioteatime.apiservice.service.TopicService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TopicController.class)
class TopicControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TopicService topicService;

    Organization organization;
    OrganizationDto organizationDto;
    Place place;
    MqttSensor mqttSensor;
    MqttSensorDto mqttSensorDto;
    Topic topic1;
    Topic topic2;
    TopicDto topicDto1;
    TopicDto topicDto2;
    List<TopicDto> topics;


    @BeforeEach
    void setUp() {

        organization = new Organization();
        organization.setId(1);
        organization.setName("nhnacademy");
        organization.setOrganizationCode("1234");
        organization.setElectricityBudget(100000L);

        place = new Place();
        place.setId(1);
        place.setPlaceName("오피스");
        place.setOrganization(organization);

        mqttSensor = new MqttSensor();
        mqttSensor.setId(1);
        mqttSensor.setName("testMqttSensor");
        mqttSensor.setModelName("testMqttModel");
        mqttSensor.setIp("192.168.0.1");
        mqttSensor.setPort("502");
        mqttSensor.setAlive(Alive.DOWN);
        mqttSensor.setOrganization(organization);
        mqttSensor.setPlace(place);

        topic1 = new Topic();
        topic1.setId(1);
        topic1.setTopic("testTopic");
        topic1.setDescription("테스트 토픽입니다.");
        topic1.setMqttSensor(mqttSensor);
        topicDto1 = new TopicDto();
        BeanUtils.copyProperties(topic1, topicDto1);

        topic2 = new Topic();
        topic2.setId(2);
        topic2.setTopic("testTopic2");
        topic2.setDescription("테스트2 토픽입니다.");
        topic2.setMqttSensor(mqttSensor);
        topicDto2 = new TopicDto();
        BeanUtils.copyProperties(topic2, topicDto2);

        topics = new ArrayList<>();
        topics.add(topicDto1);
        topics.add(topicDto2);
    }

    @Test
    @DisplayName("센서아이디에 해당하는 토픽 리스트를 가져옴")
    void getTopicsBySensorId() throws Exception {
        given(topicService.getTopicsBySensorId(anyInt())).willReturn(topics);

        ResultActions result = mockMvc.perform(get("/sensors/mqtt/{sensorId}/topics", 1));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].topic_id").value(1))
                .andExpect(jsonPath("$[0].topic").value("testTopic"))
                .andExpect(jsonPath("$[0].description").value("테스트 토픽입니다."));
    }

    @Test
    void getTopicByTopicId() throws Exception {
        given(topicService.getTopicByTopicId(anyInt())).willReturn(topicDto1);

        ResultActions result = mockMvc.perform(get("/sensors/mqtt/{sensorId}/topics/{topicId}", 1, 1));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.topic_id").value(1))
                .andExpect(jsonPath("$.topic").value("testTopic"))
                .andExpect(jsonPath("$.description").value("테스트 토픽입니다."));
    }

    @Test
    void addTopic() throws Exception {
        given(topicService.addTopic(anyInt(), any())).willReturn(topic1.getId());

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(topicDto1);

        ResultActions result = mockMvc.perform(post("/sensors/mqtt/{sensorId}/topics", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().string("Successfully create topic"));
    }

    @Test
    void updateTopic() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(topicDto1);

        ResultActions result = mockMvc.perform(put("/sensors/mqtt/{sensorId}/topics/{topicId}", 1, 1)
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));


        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Successfully update topic"));
    }

    @Test
    void deleteTopic() throws Exception {
        ResultActions result = mockMvc.perform(delete("/sensors/mqtt/{sensorId}/topics/{topicId}", 1, 1));

        result.andDo(print())
                .andExpect(status().isNoContent());
    }
}