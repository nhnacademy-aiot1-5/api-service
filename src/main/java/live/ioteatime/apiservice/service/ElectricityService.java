package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.ElectricityRequestDto;

import java.util.List;

public interface ElectricityService<T> {
    T getElectricityByDate(ElectricityRequestDto electricityRequestDto);

    List<T> getDailyElectricitiesByDate(ElectricityRequestDto electricityRequestDto);
}
