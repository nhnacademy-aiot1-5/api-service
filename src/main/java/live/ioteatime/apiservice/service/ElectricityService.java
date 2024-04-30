package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.ElectricityResponseDto;

import java.util.List;

public interface ElectricityService<T> {
    T getElectricityByDate(ElectricityRequestDto electricityRequestDto);

    List<ElectricityResponseDto> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto);
}
