package live.ioteatime.apiservice.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(ModbusSensorController.class)
class ModbusSensorControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    void getSupportedModbusSensors() {
    }

    @Test
    void getModbusSensors() {
    }

    @Test
    void getModbusSensor() {
    }

    @Test
    void addModbusSensor() {
    }

    @Test
    void updateModbusSensor() {
    }

    @Test
    void updateHealth() {
    }

    @Test
    void deleteModbusSensor() {
    }
}