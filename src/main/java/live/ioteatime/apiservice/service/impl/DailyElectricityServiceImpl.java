package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.DailyElectricity;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.DailyElectricityRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyElectricityServiceImpl implements ElectricityService<DailyElectricity> {
    private final DailyElectricityRepository dailyElectricityRepository;

    @Override
    public DailyElectricity getElectricityByDate(LocalDate localDate) {
        return dailyElectricityRepository.findByTime(localDate)
                .orElseThrow(() -> new ElectricityNotFoundException("Daily electricity not found for " + localDate));
    }

    @Override
    public List<DailyElectricity> getElectricitiesByDate(LocalDate localDate) {
        LocalDate startOfMonth = localDate.withDayOfMonth(1);
        LocalDate endOfMonth = localDate.withDayOfMonth(localDate.lengthOfMonth());
        return dailyElectricityRepository.findAllByTimeBetween(startOfMonth, endOfMonth);
    }

}
