package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.place.PlaceRequestDto;
import live.ioteatime.apiservice.dto.place.PlaceResponseDto;

import java.util.List;

public interface PlaceService {
    Place getPlace(int placeId);

    List<PlaceResponseDto> getPlaces(int organizationId);

    Place savePlace(PlaceRequestDto placeRequestDto);

    PlaceResponseDto updatePlace(int placeId, String placeName);

    void deletePlace(int placeId);
}
