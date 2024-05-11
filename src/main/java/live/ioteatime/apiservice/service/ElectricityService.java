package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.electricity.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;

import java.time.LocalDateTime;
import java.util.List;

public interface ElectricityService {
    ElectricityResponseDto getElectricityByDate(ElectricityRequestDto electricityRequestDto);

    List<ElectricityResponseDto> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto);

    ElectricityResponseDto getCurrentElectricity();

    ElectricityResponseDto getLastElectricity();

    List<ElectricityResponseDto> getTotalElectricitiesByDate(LocalDateTime localDateTime, int organizationId);
}
