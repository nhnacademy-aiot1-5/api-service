package live.ioteatime.apiservice.domain;

import lombok.Getter;

@Getter
public enum DemandCharge {
    SPRING_FALL(130.3),
    SUMMER(98.6),
    WINTER(142.6);

    private final double demandCharge;

    DemandCharge(double demandCharge) {
        this.demandCharge = demandCharge;
    }
}
