package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.repository.DailyPredictedElectricityRepository;
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
    private final DailyPredictedElectricityRepository predictedElectricityRepository;

    @Override
    public List<ElectricityResponseDto> getCurrentMonthPredictions(LocalDateTime requestTime, int organizationId) {
        LocalDateTime start = requestTime.withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = YearMonth.from(requestTime).atEndOfMonth().atTime(0, 0, 0);
        return predictedElectricityRepository
                .findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(start, end, organizationId, -1)
                .stream()
                .map(data -> new ElectricityResponseDto(data.getTime(), data.getKwh(), data.getBill()))
                .collect(Collectors.toList());
    }

    @Override
    public List<ElectricityResponseDto> getNextMonthPrediction(LocalDateTime requestTime, int organizationId) {
        LocalDateTime start = requestTime.plusMonths(1).withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0)
                .withNano(0);
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        return getFromRepository(start, end, organizationId);
    }

    @Override
    public List<ElectricityResponseDto> getThisMonthPrediction(LocalDateTime requestTime, int organizationId) {
        LocalDateTime start = requestTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = start.plusMonths(1).minusDays(1);
        return getFromRepository(start, end, organizationId);
    }

    private List<ElectricityResponseDto> getFromRepository(LocalDateTime start, LocalDateTime end, int organizationId) {
        return predictedElectricityRepository
                .findAllByTimeBetweenAndOrganizationIdAndChannelIdOrderByTimeAsc(start, end, organizationId, -1)
                .stream()
                .map(data -> new ElectricityResponseDto(data.getTime(), data.getKwh(), data.getBill()))
                .collect(Collectors.toList());
    }

}
