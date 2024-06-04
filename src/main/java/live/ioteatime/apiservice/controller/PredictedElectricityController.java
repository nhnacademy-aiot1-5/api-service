package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.dto.electricity.PreciseElectricityResponseDto;
import live.ioteatime.apiservice.service.PredictedElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/predicted")
@RequiredArgsConstructor
@Tag(name = "Predicted Electricity", description = "전력량 예측 API")
public class PredictedElectricityController {

    private final PredictedElectricityService predictedElectricityService;

    @GetMapping
    @Operation(summary = "금월 총전력량 예측 데이터 조회", description = "요청일부터 월말까지의 총전력량 예측 데이터를 조회합니다.")
    public ResponseEntity<List<PreciseElectricityResponseDto>> getMonthlyPredictedValues(
            @RequestParam("organizationId") int organizationId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam("requestTime") LocalDateTime requestTime) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(predictedElectricityService.getCurrentMonthPredictions(requestTime, organizationId));
    }

    @GetMapping("/next-month")
    @Operation(summary = "다음 달 예측 전력량 조회")
    public ResponseEntity<List<PreciseElectricityResponseDto>> getNextMonthPredictedValues(
            @RequestParam("organizationId") int organizationId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam("requestTime") LocalDateTime requestTime) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(predictedElectricityService.getNextMonthPrediction(requestTime, organizationId));
    }

    @GetMapping("/this-month")
    @Operation(summary = "이번 달 예측 전력량 조회")
    public ResponseEntity<List<PreciseElectricityResponseDto>> getThisMonthPredictedValues(
            @RequestParam("organizationId") int organizationId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam("requestTime") LocalDateTime requestTime) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(predictedElectricityService.getThisMonthPrediction(requestTime, organizationId));
    }

}
