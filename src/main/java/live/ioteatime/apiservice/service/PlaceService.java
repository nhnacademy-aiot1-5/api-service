package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.PlaceRequestDto;
import live.ioteatime.apiservice.dto.PlaceWithoutOrganizationDto;

import java.util.List;

public interface PlaceService {
    Place getPlace(int placeId);

    List<PlaceWithoutOrganizationDto> getPlaces(int organizationId);

    Place savePlace(PlaceRequestDto placeRequestDto);
}
