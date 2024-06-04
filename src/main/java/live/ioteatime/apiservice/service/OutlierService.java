package live.ioteatime.apiservice.service;

import live.ioteatime.apiservice.domain.Outlier;
import live.ioteatime.apiservice.dto.OutlierDto;

import java.util.List;

/**
 * 이상치 서비스에 필요한 기능을 구현한 서비스 입니다.
 */
public interface OutlierService {

    /**
     * 조직 ID에 해당하는 이상치 값들을 가져옵니다.
     * @param organizationId 이상치를 가져올 조직의 아이디입니다.
     * @return 아웃라이어DTO의 리스트를 반환합니다.
     */
    List<OutlierDto> getOutlierByOrganizationId(int organizationId);

    /**
     * 아웃라이어 DTO 의 정보를 데이터베이스에 저장합니다.
     * @param outlierDto 데이터베이스에 저장할 DTO 입니다.
     * @return 저장된 Outlier 를 반환합니다.
     */
    Outlier createOutlier(OutlierDto outlierDto);

    /**
     * 아웃라이어 ID에 해당하는 Outlier의 flag 상태를 flag로  변경합니다.
     * @param id   변경할 아웃라이어 ID 입니다.
     * @param flag 변경할 아웃라이어 값입니다.
     * @return 변경된 아웃라이어의 ID값을 반환합니다.
     */
    int updateOutlier(int id, int flag);
}
