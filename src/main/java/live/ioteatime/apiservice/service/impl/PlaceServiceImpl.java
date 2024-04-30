package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.PlaceRequestDto;
import live.ioteatime.apiservice.dto.PlaceWithoutOrganizationDto;
import live.ioteatime.apiservice.exception.OrganizationNotFoundException;
import live.ioteatime.apiservice.exception.PlaceNotFoundException;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public List<PlaceWithoutOrganizationDto> getPlaces(int organizationId) {
        List<Place> placeList = placeRepository.findAllByOrganization_Id(organizationId);
        List<PlaceWithoutOrganizationDto> dtoList = new ArrayList<>();
        for (Place place: placeList) {
            PlaceWithoutOrganizationDto dto = new PlaceWithoutOrganizationDto();
            BeanUtils.copyProperties(place, dto);
            dtoList.add(dto);
        }
        return dtoList;
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
