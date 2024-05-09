package live.ioteatime.apiservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
public enum DemandCharge {
    SPRING_FALL(130.3),
    SUMMER(98.6),
    WINTER(142.6);

    private final double demandCharge;
}
