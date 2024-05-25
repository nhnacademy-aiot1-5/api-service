package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.place.PlaceDto;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

class PlaceServiceTest {

    @Mock
    PlaceRepository placeRepository;

    @Mock
    OrganizationRepository organizationRepository;

    @InjectMocks
    PlaceServiceImpl placeService;

    Organization organization;
    Place place1;
    Place place2;
    PlaceDto placeDto1;

    List<Place> places;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        organization = new Organization();
        organization.setId(1);
        organization.setName("nhnacademy");
        organization.setOrganizationCode("1234");

        place1 = new Place();
        place1.setId(1);
        place1.setOrganization(organization);
        place1.setPlaceName("office");

        placeDto1 = new PlaceDto();
        BeanUtils.copyProperties(place1, placeDto1);

        place2 = new Place();
        place2.setId(2);
        place2.setOrganization(organization);
        place2.setPlaceName("class_A");


        places = new ArrayList<>();
        places.add(place1);
        places.add(place2);
    }

    @Test
    void getPlace() {
        given(placeRepository.findById(any())).willReturn(Optional.of(place1));

        PlaceDto response = placeService.getPlace(1);

        assertThat(response.getPlaceName()).isEqualTo("office");
    }

    @Test
    void getPlaces() {
        int organizationId = 1;
        given(placeRepository.findAllByOrganization_Id(anyInt())).willReturn(places);

        List<PlaceDto> response = placeService.getPlaces(organizationId);

        assertThat(response.size()).isEqualTo(2);
        assertThat(response.get(0).getPlaceName()).isEqualTo("office");
        assertThat(response.get(1).getPlaceName()).isEqualTo("class_A");
    }

    @Test
    void createPlace() {
        given(organizationRepository.findById(anyInt())).willReturn(Optional.of(organization));
        given(placeRepository.save(any())).willReturn(place1);

        assertThat(placeService.createPlace(placeDto1)).isEqualTo(placeDto1);
    }

    @Test
    void updatePlace() {
        given(placeRepository.findById(anyInt())).willReturn(Optional.of(place1));
        given(placeRepository.save(any())).willReturn(place2);

        placeDto1 = placeService.updatePlace(place1.getId(), place1.getPlaceName());

        assertThat(placeDto1.getPlaceName()).isEqualTo("office");
    }

    @Test
    void deletePlace() {
        int placeId = 1;

        placeService.deletePlace(placeId);

        verify(placeRepository).deleteById(placeId);
    }
}