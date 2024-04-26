package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.PlaceRequestDto;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.PlaceNotFoundException;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {
    private final PlaceRepository placeRepository;
    private final OrganizationRepository organizationRepository;

    @Override
    public Place getPlace(int placeId) {
        return placeRepository.findById(placeId).orElseThrow(PlaceNotFoundException::new);
    }

    @Override
    public List<Place> getPlaces(int organizationId) {
        return placeRepository.findAllByOrganization_Id(organizationId);
    }

    @Override
    public Place savePlace(PlaceRequestDto placeRequestDto) {
        Organization organization = organizationRepository.findById(placeRequestDto.getOrganizationId())
                .orElseThrow(OrganizationNotFoundException::new);
        Place place = new Place(0,
                placeRequestDto.getPlaceName(),
                organization);

        return placeRepository.save(place);
    }
}
