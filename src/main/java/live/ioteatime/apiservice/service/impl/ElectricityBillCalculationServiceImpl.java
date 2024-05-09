package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.service.ElectricityBillCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

/*
    일반용(갑) 전력 요금을 계산하기 위한 서비스입니다.
 */

@Service
@RequiredArgsConstructor
public class ElectricityBillCalculationServiceImpl implements ElectricityBillCalculationService {
    @Override
    public Long calculateElectricityBill(Long kwh) {
        long electricityBill = getDemandCharge() + getDemandCharge() + getClimateChangeCharge(kwh) + getFuelCostAdjustmentCharge(kwh);

        return electricityBill + getVAT(electricityBill) + getElectricityIndustryInfraFund(electricityBill);
    }

    @Override
    public Long getGeneralCharge() {
        return (long) 7170 * 30;
    }

    @Override
    public Long getDemandCharge() {
        int currentMonth = LocalDateTime.now().getMonthValue();

        Map<String, Double> GENERAL_A_I_DEMAND_CHARGE = Map.of(
                "SUMMER", 98.6,
                "WINTER", 142.6,
                "SPRING_FALL", 130.3
        );

        double chargePerMonth = 0;

        if (currentMonth >= 6 && currentMonth <= 8) {
            chargePerMonth = GENERAL_A_I_DEMAND_CHARGE.get("SUMMER");
        } else if (currentMonth <= 2 || currentMonth >= 11) {
            chargePerMonth = GENERAL_A_I_DEMAND_CHARGE.get("WINTER");
        } else {
            chargePerMonth = GENERAL_A_I_DEMAND_CHARGE.get("SPRING_FALL");
        }

        return (long) Math.floor(chargePerMonth * 30);
    }

    @Override
    public Long getClimateChangeCharge(Long kwh) {
        return (long) Math.floor(kwh * 9);
    }

    @Override
    public Long getFuelCostAdjustmentCharge(Long kwh) {
        return (long) Math.floor(kwh * 5);
    }

    public Long getVAT(Long electricityBill) {
        return Math.round(electricityBill * 0.1);
    }

    public Long getElectricityIndustryInfraFund(Long electricityBill) {
        return (long) Math.floor(electricityBill * 0.037);
    }
}
