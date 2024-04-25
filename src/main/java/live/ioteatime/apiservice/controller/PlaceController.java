package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.PlaceResponseDto;
import live.ioteatime.apiservice.dto.PlaceRequestDto;
import live.ioteatime.apiservice.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Place 컨트롤러", description = "구역을 추가, 수정, 불러올 수 있는 엔드 포인트 입니다.")
public class PlaceController {
    private final PlaceService placeService;

    @GetMapping("/place")
    @Operation(summary = "id 별로 place 가져오기")
    public ResponseEntity<PlaceResponseDto> getPlace(@RequestParam(name = "place_id") int placeId) {
        Place place = placeService.getPlace(placeId);
        PlaceResponseDto placeResponseDto = new PlaceResponseDto(place.getId(), place.getPlaceName());
        return ResponseEntity.ok(placeResponseDto);
    }

    @GetMapping("/places")
    @Operation(summary = "organization id 별로 place 리스트 가져오기")
    public ResponseEntity<List<PlaceResponseDto>> getPlaces(@RequestParam(name = "organization_id") int organizationId) {
        List<Place> places = placeService.getPlaces(organizationId);
        List<PlaceResponseDto> placeResponseDtos = new ArrayList<>();
        for (Place p : places) {
            placeResponseDtos.add(new PlaceResponseDto(p.getId(), p.getPlaceName()));
        }
        return ResponseEntity.ok(placeResponseDtos);
    }

    @PostMapping("/place")
    @Operation(summary = "구역 저장하기")
    public PlaceResponseDto registerPlace(@RequestBody PlaceRequestDto placeRequestDto) {
        Place place = placeService.savePlace(placeRequestDto);

        return new PlaceResponseDto(place.getId(), place.getPlaceName());
    }
}
