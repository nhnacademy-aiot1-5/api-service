package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Outlier;
import live.ioteatime.apiservice.dto.OutlierDto;
import live.ioteatime.apiservice.repository.OutlierRepository;
import live.ioteatime.apiservice.service.OutlierService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutlierServiceImpl implements OutlierService {

    private final OutlierRepository outlierRepository;

    @Override
    public List<OutlierDto> getUnsolvedOutlier() {
        return outlierRepository.findAllByFlag(0);
    }

    @Override
    public int createOutlier(OutlierDto outlierDto) {
        Outlier outlier = new Outlier();
        BeanUtils.copyProperties(outlierDto, outlier);
        outlierRepository.save(outlier);

        return outlier.getId();
    }

    @Override
    public int updateOutlier(int id, int flag) {
        Outlier outlier = outlierRepository.findById(id).orElse(null);
        outlier.setFlag(flag);
        outlierRepository.save(outlier);
        return outlier.getId();
    }
}
