package live.ioteatime.apiservice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutlierDto {

    int id;

    String place;

    String type;

    long time;

    double outlierValue;

    int flag;

    int organizationId;

}
