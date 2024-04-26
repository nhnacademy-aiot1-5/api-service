package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.PlaceRequestDto;

import java.util.List;

public interface PlaceService {
    Place getPlace(int placeId);

    List<Place> getPlaces(int organizationId);

    Place savePlace(PlaceRequestDto placeRequestDto);
}
