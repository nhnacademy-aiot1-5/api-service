package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.MonthlyElectricity;
import live.ioteatime.apiservice.dto.electricity.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.MonthlyElectricityRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service("monthlyElectricityService")
@RequiredArgsConstructor
public class MonthlyElectricityServiceImpl implements ElectricityService {
    private final MonthlyElectricityRepository monthlyElectricityRepository;

    @Override
    public ElectricityResponseDto getElectricityByDate(ElectricityRequestDto electricityRequestDto) {
        MonthlyElectricity.Pk pk = new MonthlyElectricity.Pk(electricityRequestDto.getChannelId(), electricityRequestDto.getTime());
        MonthlyElectricity monthlyElectricity = monthlyElectricityRepository.findMonthlyElectricityByPk(pk)
                .orElseThrow(() -> new ElectricityNotFoundException("monthly Electricity not found."));
        return new ElectricityResponseDto(monthlyElectricity.getPk().getTime(), monthlyElectricity.getKwh());
    }

    @Override
    public List<ElectricityResponseDto> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        LocalDateTime endOfMonth = electricityRequestDto.getTime().withDayOfMonth(1).minusDays(1);
        LocalDateTime startOfTwelveMonthsBefore = endOfMonth.minusMonths(11).withDayOfMonth(1);

        List<MonthlyElectricity> monthlyElectricities = monthlyElectricityRepository.findAllByPkChannelIdAndPkTimeBetween(
                electricityRequestDto.getChannelId(),
                startOfTwelveMonthsBefore,
                endOfMonth
        );
        List<ElectricityResponseDto> responseDtos = new ArrayList<>();

        for (MonthlyElectricity monthlyElectricity : monthlyElectricities) {
            responseDtos.add(new ElectricityResponseDto(monthlyElectricity.getPk().getTime(), monthlyElectricity.getKwh()));
        }
        return responseDtos;
    }
}
