package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.DailyElectricity;
import live.ioteatime.apiservice.dto.ElectricityRequestDto;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.DailyElectricityRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyElectricityServiceImpl implements ElectricityService<DailyElectricity> {
    private final DailyElectricityRepository dailyElectricityRepository;

    @Override
    public DailyElectricity getElectricityByDate(ElectricityRequestDto electricityRequestDto) {
        DailyElectricity.Pk pk = new DailyElectricity.Pk(electricityRequestDto.getOrganizationId(), electricityRequestDto.getTime());
        return dailyElectricityRepository.findByPk(pk)
                .orElseThrow(() -> new ElectricityNotFoundException("Daily electricity not found for " + pk));
    }

    @Override
    public List<DailyElectricity> getDailyElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        LocalDateTime localDateTime = electricityRequestDto.getTime();
        LocalDateTime startOfMonth = localDateTime.withDayOfMonth(1);
        LocalDateTime endOfMonth = localDateTime.withDayOfMonth(localDateTime.toLocalDate().lengthOfMonth());

        return dailyElectricityRepository.findAllByPkOrganizationIdAndPkTimeBetween(
                electricityRequestDto.getOrganizationId(),
                startOfMonth,
                endOfMonth
        );
    }

}
