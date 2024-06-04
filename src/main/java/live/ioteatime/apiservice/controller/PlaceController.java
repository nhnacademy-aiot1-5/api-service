package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.dto.place.PlaceDto;
import live.ioteatime.apiservice.service.PlaceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "Place", description = "장소 API")
public class PlaceController {

    private final PlaceService placeService;

    @GetMapping("/place")
    @Operation(summary = "장소 단일 조회", description = "장소 정보를 단일 조회합니다.")
    public ResponseEntity<PlaceDto> getPlace(@RequestParam(name = "place_id") int placeId) {
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getPlace(placeId));
    }

    @GetMapping("/places")
    @Operation(summary = "조직의 장소 리스트 조회", description = "조직의 모든 장소를 조회합니다.")
    public ResponseEntity<List<PlaceDto>> getPlaces(int organizationId) {
        return ResponseEntity.status(HttpStatus.OK).body(placeService.getPlaces(organizationId));
    }

    @PostMapping("/place")
    @Operation(summary = "장소 등록", description = "새로운 장소를 등록합니다.")
    public ResponseEntity<PlaceDto> registerPlace(@RequestBody PlaceDto placeDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.createPlace(placeDto));
    }

    @PutMapping("/place")
    @Operation(summary = "장소 이름 수정", description = "장소 이름을 수정합니다.")
    public ResponseEntity<PlaceDto> updatePlace(int placeId, String placeName) {
        return ResponseEntity.status(HttpStatus.OK).body(placeService.updatePlace(placeId, placeName));
    }

    @DeleteMapping("/place")
    @Operation(summary = "장소 삭제", description = "장소를 삭제합니다.")
    public ResponseEntity<String> deletePlace(int placeId) {
        placeService.deletePlace(placeId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

}
