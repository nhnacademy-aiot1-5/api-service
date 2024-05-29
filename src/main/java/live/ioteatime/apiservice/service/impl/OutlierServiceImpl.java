package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.Organization;
import live.ioteatime.apiservice.domain.Outlier;
import live.ioteatime.apiservice.dto.OutlierDto;
import live.ioteatime.apiservice.repository.OutlierRepository;
import live.ioteatime.apiservice.service.OutlierService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutlierServiceImpl implements OutlierService {

    private final OutlierRepository outlierRepository;

    @Override
    public List<OutlierDto> getUnsolvedOutlier() {
        List<Outlier> outlierList=  outlierRepository.findAllByFlag(0);
        List<OutlierDto> outlierDtoList=new ArrayList<>();
        for (Outlier outlier : outlierList) {
            OutlierDto outlierDto=new OutlierDto();
            BeanUtils.copyProperties(outlier,outlierDto);
            outlierDtoList.add(outlierDto);
        }
        return outlierDtoList;
    }

    @Override
    public Outlier createOutlier(OutlierDto outlierDto) {
        Outlier outlier = new Outlier();
        BeanUtils.copyProperties(outlierDto, outlier);
        Organization organization = new Organization();
        organization.setId(outlierDto.getOrganizationId());
        outlier.setOrganization(organization);
        outlierRepository.save(outlier);

        return outlier;
    }

    @Override
    public int updateOutlier(int id, int flag) {
        Outlier outlier = outlierRepository.findById(id).orElse(null);
        outlier.setFlag(flag);
        outlierRepository.save(outlier);
        return outlier.getId();
    }
}
