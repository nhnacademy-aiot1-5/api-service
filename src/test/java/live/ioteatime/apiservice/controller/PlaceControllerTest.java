package live.ioteatime.apiservice.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.place.PlaceDto;
import live.ioteatime.apiservice.service.PlaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = PlaceController.class)
class PlaceControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    PlaceService placeService;

    Organization organization;

    Place place1;
    Place place2;

    List<PlaceDto> placeDtoList;

    PlaceDto placeDto1;
    PlaceDto placeDto2;

    @BeforeEach
    void setUp() throws Exception {

        organization = new Organization();
        organization.setName("nhnacademy");
        organization.setId(1);
        organization.setOrganizationCode("1234");
        organization.setElectricityBudget(100000L);

        place1 = new Place();
        place1.setId(1);
        place1.setPlaceName("오피스");
        place1.setOrganization(organization);
        placeDto1 = new PlaceDto();
        BeanUtils.copyProperties(place1,placeDto1);

        place2 = new Place();
        place2.setId(2);
        place2.setPlaceName("Class_A");
        place2.setOrganization(organization);
        placeDto2 = new PlaceDto();
        BeanUtils.copyProperties(place2,placeDto2);

        placeDtoList = List.of(placeDto1, placeDto2);
    }

    @Test
    void getPlace() throws Exception {
        given(placeService.getPlace(anyInt())).willReturn(placeDto1);

        ResultActions result = mockMvc.perform(get("/place").param("place_id", "1"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.place_id").value(1))
                .andExpect(jsonPath("$.place_name").value("오피스"));
    }

    @Test
    void getPlaces() throws Exception {
        given(placeService.getPlaces(anyInt())).willReturn(placeDtoList);

        ResultActions result = mockMvc.perform(get("/places").param("organizationId", "1"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].place_id").value(1))
                .andExpect(jsonPath("$[0].place_name").value("오피스"))
                .andExpect(jsonPath("$[1].place_id").value(2))
                .andExpect(jsonPath("$[1].place_name").value("Class_A"));
    }

    @Test
    void registerPlace() throws Exception {
        given(placeService.createPlace(any())).willReturn(placeDto1);

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(placeDto1);

        ResultActions result = mockMvc.perform(post("/place")
                .param("placeDto", placeDto1.toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(json));

        result.andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.place_id").value(1))
                .andExpect(jsonPath("$.place_name").value("오피스"));
    }

    @Test
    void updatePlace() throws Exception {
        placeDto1.setPlaceName("Class_B");
        given(placeService.updatePlace(anyInt(), anyString())).willReturn(placeDto1);

        ResultActions result = mockMvc.perform(put("/place")
                .param("placeId", String.valueOf(placeDto1.getId()))
                .param("placeName", "Class_B"));

        result.andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.place_id").value(1))
                .andExpect(jsonPath("$.place_name").value("Class_B"));
    }

    @Test
    void deletePlace() throws Exception {
        ResultActions result = mockMvc.perform(delete("/place")
        .param("placeId", String.valueOf(placeDto1.getId())));

        result.andDo(print())
                .andExpect(status().isNoContent());

    }
}