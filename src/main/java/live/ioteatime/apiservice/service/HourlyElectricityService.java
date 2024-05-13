package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.electricity.PreciseElectricityResponseDto;

import java.util.List;

public interface HourlyElectricityService {
    List<PreciseElectricityResponseDto> getOneHourTotalElectricties(int organizationId);
}
