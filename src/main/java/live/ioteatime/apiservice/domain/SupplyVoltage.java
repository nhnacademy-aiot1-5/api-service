package live.ioteatime.apiservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SupplyVoltage {
    LOW_VOLTAGE(6160),
    HIGH_VOLTAGE_A_OPTION_I(7170),
    HIGH_VOLTAGE_A_OPTION_II(8230),
    HIGH_VOLTAGE_B_OPTION_I(7170),
    HIGH_VOLTAGE_B_OPTION_II(8230);

    private final int generalCharge;
}
