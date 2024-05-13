package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.electricity.PreciseElectricityResponseDto;
import java.time.LocalDateTime;
import java.util.List;

public interface PredictedElectricityService {
    List<PreciseElectricityResponseDto> getCurrentMonthPredictions(LocalDateTime requestTime);
}
