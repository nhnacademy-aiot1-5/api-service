package live.ioteatime.apiservice.controller;

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
    public ResponseEntity<List<PreciseElectricityResponseDto>> getMonthlyPredictedValues(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam("LocalDateTime") LocalDateTime requestTime){
        return ResponseEntity.status(HttpStatus.OK)
                .body(predictedElectricityService.getCurrentMonthPredictions(requestTime));
    }
}
