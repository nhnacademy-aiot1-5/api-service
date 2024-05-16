package live.ioteatime.apiservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.apiservice.dto.channel.ChannelDto;
import live.ioteatime.apiservice.dto.place.PlaceDto;
import live.ioteatime.apiservice.dto.sensor.ModbusSensorDto;
import live.ioteatime.apiservice.service.impl.ChannelServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ChannelController.class)
class ChannelControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ChannelServiceImpl channelService;

    ObjectMapper objectMapper;

    ChannelDto channelDto;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        ModbusSensorDto sensorDto = new ModbusSensorDto();
        sensorDto.setId(1);
        PlaceDto placeDto = new PlaceDto();
        placeDto.setId(1);
        channelDto = new ChannelDto(1, "main", 8000, "UINT", 4, 1,
                1, "office", sensorDto, placeDto);
    }

    @Test
    void getChannels() throws Exception {
        given(channelService.getChannelList(anyInt())).willReturn(List.of(channelDto));

        ResultActions result = mockMvc.perform(get("/sensors/modbus/1/channels")
                .header("X-USER-ID", "admin"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].channel_name").value("main"));
    }

    @Test
    void getChannelsFromPlace() throws Exception {
        given(channelService.getChannelListByPlace(anyInt())).willReturn(List.of(channelDto));

        ResultActions result = mockMvc.perform(get("/sensors/modbus/channels/1")
                .header("X-USER-ID", "admin"));

        result.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].place_id").value(1));
    }

    @Test
    void existChannelCheck() throws Exception {
        given(channelService.existChannelCheck(anyInt())).willReturn(false);

        ResultActions result = mockMvc.perform(get("/sensors/modbus/1/exist-channels")
                .header("X-USER-ID", "admin"));

        result.andExpect(status().isOk())
                .andExpect(content().string("false"));
    }

    @Test
    void createChannel() throws Exception {
        given(channelService.createChannel(anyInt(), any())).willReturn(1);

        ResultActions result = mockMvc.perform(post("/sensors/modbus/1/channels")
                .header("X-USER-ID", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"channel_name\":\"main\", \"address\":8000, \"type\":\"UINT\", \"place_id\":1}"));

        result.andExpect(status().isCreated())
                .andExpect(content().string(String.valueOf(1)));
    }

    @Test
    void updateChannelPlace() throws Exception {
        given(channelService.updateChannelPlace(anyInt(), any())).willReturn(2);

        ResultActions result = mockMvc.perform(put("/sensors/modbus/1/change-place")
                .header("X-USER-ID", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"channel_name\":\"main\", \"address\":8000, \"type\":\"UINT\", \"place_id\":2}"));

        result.andExpect(status().isOk()).andExpect(content().string(String.valueOf(2)));
    }

    @Test
    void updateChannelInfo() throws Exception {
        given(channelService.updateChannelInfo(anyInt(), any())).willReturn(1);

        ResultActions result = mockMvc.perform(put("/sensors/modbus/1/change-info")
                .header("X-USER-ID", "admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"channel_name\":\"ac_indoor\", \"address\":8200, \"type\":\"UINT\", \"place_id\":1}"));

        result.andExpect(status().isOk()).andExpect(content().string(String.valueOf(1)));
    }

    @Test
    void deleteChannel() throws Exception {
        willDoNothing().given(channelService).deleteChannel(anyInt(), anyInt());

        ResultActions result = mockMvc.perform(delete("/sensors/modbus/1/channels/1")
                .header("X-USER-ID", "admin"));

        result.andExpect(status().isNoContent());
    }
}