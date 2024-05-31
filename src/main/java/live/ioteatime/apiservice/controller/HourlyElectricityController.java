package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.dto.electricity.PreciseElectricityResponseDto;
import live.ioteatime.apiservice.service.HourlyElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hourly")
@RequiredArgsConstructor
@Tag(name = "Hourly Electricity", description = "시간별 전력량 API")
public class HourlyElectricityController {

    private final HourlyElectricityService hourlyElectricityService;

    @GetMapping("/electricities/total")
    @Operation(summary = "최근 1시간 전력량 조회", description = "최근 1시간 동안의 5분 단위 전력량을 조회합니다.")
    public ResponseEntity<List<PreciseElectricityResponseDto>> getOneHourTotalElectricties(@RequestParam int organizationId) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(hourlyElectricityService.getOneHourTotalElectricties(organizationId));
    }

}
