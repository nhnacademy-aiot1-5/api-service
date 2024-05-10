package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.electricity.PreciseKwhResponseDto;

import java.util.List;

public interface HourlyElectricityService {
    List<PreciseKwhResponseDto> getOneHourTotalElectricties(int organizationId);
}
