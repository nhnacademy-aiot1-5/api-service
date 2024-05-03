package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.place.PlaceRequest;
import live.ioteatime.apiservice.dto.place.PlaceResponseDto;
import live.ioteatime.apiservice.dto.place.PlaceWithoutOrganizationDto;

import java.util.List;

public interface PlaceService {
    Place getPlace(int placeId);

    List<PlaceWithoutOrganizationDto> getPlaces(int organizationId);

    Place savePlace(PlaceRequest placeRequestDto);

    PlaceResponseDto updatePlace(int placeId, String placeName);

    void deletePlace(int placeId);
}
