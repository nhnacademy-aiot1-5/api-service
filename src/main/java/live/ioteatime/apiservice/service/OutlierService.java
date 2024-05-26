package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.dto.OutlierDto;

import java.util.List;

public interface OutlierService {
    List<OutlierDto> getUnsolvedOutlier();

    int createOutlier(OutlierDto outlierDto);

    int updateOutlier(int id, int flag);
}
