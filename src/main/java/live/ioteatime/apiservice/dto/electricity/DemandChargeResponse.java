package live.ioteatime.apiservice.dto.electricity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DemandChargeResponse {

    private Long demandCharge;

}
