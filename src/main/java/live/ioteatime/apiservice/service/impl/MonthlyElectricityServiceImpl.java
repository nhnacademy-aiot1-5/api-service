package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.MonthlyElectricity;
import live.ioteatime.apiservice.dto.ElectricityRequestDto;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.MonthlyElectricityRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MonthlyElectricityServiceImpl implements ElectricityService<MonthlyElectricity> {
    private final MonthlyElectricityRepository monthlyElectricityRepository;

    @Override
    public MonthlyElectricity getElectricityByDate(ElectricityRequestDto electricityRequestDto) {
        MonthlyElectricity.Pk pk = new MonthlyElectricity.Pk(electricityRequestDto.getOrganizationId(), electricityRequestDto.getTime());
        return monthlyElectricityRepository.findMonthlyElectricityByPk(pk)
                .orElseThrow(() -> new ElectricityNotFoundException("monthly Electricity not found."));
    }

    @Override
    public List<MonthlyElectricity> getDailyElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        return null;
    }
}
