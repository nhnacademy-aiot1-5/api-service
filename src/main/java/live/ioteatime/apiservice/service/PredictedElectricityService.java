package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface PredictedElectricityService {
    List<ElectricityResponseDto> getCurrentMonthPredictions(LocalDateTime requestTime, int organizationId);

    List<ElectricityResponseDto> getNextMonthPrediction(LocalDateTime requestTime, int organizationId);

    List<ElectricityResponseDto> getThisMonthPrediction(LocalDateTime requestTime, int organizationId);
}
