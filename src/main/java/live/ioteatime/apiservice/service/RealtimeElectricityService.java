package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.electricity.RealtimeElectricityResponseDto;

import java.util.List;

public interface RealtimeElectricityService {
    List<RealtimeElectricityResponseDto> getRealtimeElectricity(int organizationId);
}
