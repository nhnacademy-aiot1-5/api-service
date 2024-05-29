package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Outlier;
import live.ioteatime.apiservice.dto.OutlierDto;

import java.util.List;

public interface OutlierService {
    List<OutlierDto> getUnsolvedOutlier();

    Outlier createOutlier(OutlierDto outlierDto);

    int updateOutlier(int id, int flag);
}
