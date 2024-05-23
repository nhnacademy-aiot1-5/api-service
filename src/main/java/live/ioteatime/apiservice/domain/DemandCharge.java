package live.ioteatime.apiservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DemandCharge {
    SPRING_FALL(98.6),
    SUMMER(142.6),
    WINTER(130.3);

    private final double demandCharge;
}
