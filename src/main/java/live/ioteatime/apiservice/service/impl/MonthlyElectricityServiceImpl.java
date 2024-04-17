package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.DailyElectricity;
import live.ioteatime.apiservice.domain.MonthlyElectricity;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.MonthlyElectricityRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyElectricityServiceImpl implements ElectricityService<MonthlyElectricity> {
    private final MonthlyElectricityRepository monthlyElectricityRepository;

    @Override
    public MonthlyElectricity getElectricityByDate(LocalDate localDate) {
        return monthlyElectricityRepository.findMonthlyElectricityByTime(localDate)
                .orElseThrow(() -> new ElectricityNotFoundException("monthly Electricity not found."));
    }

    @Override
    public List<DailyElectricity> getElectricitiesByDate(LocalDate localDate) {
        return null;
    }
}
