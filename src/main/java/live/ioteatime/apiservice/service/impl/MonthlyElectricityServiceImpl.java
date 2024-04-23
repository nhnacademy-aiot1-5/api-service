package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.MonthlyElectricity;
import live.ioteatime.apiservice.dto.ElectricityRequestDto;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.MonthlyElectricityRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
    public List<MonthlyElectricity> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        LocalDateTime endOfMonth = electricityRequestDto.getTime().withDayOfMonth(1).minusDays(1);

        LocalDateTime startOfTwelveMonthsBefore = endOfMonth.minusMonths(11).withDayOfMonth(1);

        return monthlyElectricityRepository.findAllByPkOrganizationIdAndPkTimeBetween(
                electricityRequestDto.getOrganizationId(),
                startOfTwelveMonthsBefore,
                endOfMonth
        );
    }
}
