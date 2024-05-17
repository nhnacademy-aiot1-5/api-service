package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.place.PlaceDto;

import java.util.List;

public interface PlaceService {
    PlaceDto getPlace(int placeId);

    List<PlaceDto> getPlaces(int organizationId);

    PlaceDto createPlace(PlaceDto placeDto);

    PlaceDto updatePlace(int placeId, String placeName);

    void deletePlace(int placeId);
}
