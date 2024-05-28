package live.ioteatime.apiservice.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutlierDto {
    @JsonProperty("outlier_id")
    int id;
    String place;
    String type;
    @JsonProperty("outlier_value")
    double outlierValue;
    int flag;
}
