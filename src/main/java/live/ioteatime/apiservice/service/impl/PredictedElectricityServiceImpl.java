package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.dto.electricity.PreciseElectricityResponseDto;
import live.ioteatime.apiservice.repository.PredictedElectricityRepository;
import live.ioteatime.apiservice.service.PredictedElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PredictedElectricityServiceImpl implements PredictedElectricityService {

    private final PredictedElectricityRepository predictedElectricityRepository;


    @Override
    public List<PreciseElectricityResponseDto> getCurrentMonthPredictions(LocalDateTime requestTime) {
        LocalDateTime start = requestTime.plusDays(1);
        LocalDateTime end = YearMonth.from(requestTime).atEndOfMonth().atTime(0,0,0);
        return predictedElectricityRepository.findAllByTimeBetween(start, end)
                .stream()
                .map(data -> new PreciseElectricityResponseDto(data.getTime(), data.getKwh()))
                .collect(Collectors.toList());
    }
}
