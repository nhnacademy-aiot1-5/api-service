package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.HourlyElectricity;
import live.ioteatime.apiservice.dto.ElectricityRequestDto;
import live.ioteatime.apiservice.service.ElectricityService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class HourlyElectricityServiceImpl implements ElectricityService<HourlyElectricity> {
    @Override
    public HourlyElectricity getElectricityByDate(ElectricityRequestDto electricityRequestDto) {
        return null;
    }

    @Override
    public List<HourlyElectricity> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        return null;
    }
}
