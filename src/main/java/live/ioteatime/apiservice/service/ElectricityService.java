package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.electricity.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.dto.electricity.KwhResponseDto;

import java.util.List;

public interface ElectricityService {
    ElectricityResponseDto getElectricityByDate(ElectricityRequestDto electricityRequestDto);

    List<ElectricityResponseDto> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto);
}
