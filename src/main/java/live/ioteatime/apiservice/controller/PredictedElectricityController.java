package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.annotation.VerifyOrganization;
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
public class PredictedElectricityController {

    private final PredictedElectricityService predictedElectricityService;

    @GetMapping
    @VerifyOrganization
    public ResponseEntity<List<PreciseElectricityResponseDto>> getMonthlyPredictedValues(
            @RequestParam("organizationId") int organizationId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam("requestTime") LocalDateTime requestTime) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(predictedElectricityService.getCurrentMonthPredictions(requestTime, organizationId));
    }

    @GetMapping("/next-month")
    public ResponseEntity<List<PreciseElectricityResponseDto>> getNextMonthPredictedValues(
            @RequestParam("organizationId") int organizationId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam("requestTime") LocalDateTime requestTime) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(predictedElectricityService.getNextMonthPrediction(requestTime, organizationId));
    }

    @GetMapping("/this-month")
    public ResponseEntity<List<PreciseElectricityResponseDto>> getThisMonthPredictedValues(
            @RequestParam("organizationId") int organizationId,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam("requestTime") LocalDateTime requestTime) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(predictedElectricityService.getThisMonthPrediction(requestTime, organizationId));
    }
}
