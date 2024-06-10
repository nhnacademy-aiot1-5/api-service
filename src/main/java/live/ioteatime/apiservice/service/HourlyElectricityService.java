package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;

import java.util.List;

public interface HourlyElectricityService {
    List<ElectricityResponseDto> getOneHourTotalElectricties(int organizationId);
}
