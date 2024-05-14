package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.DemandCharge;
import live.ioteatime.apiservice.domain.SupplyVoltage;
import live.ioteatime.apiservice.service.ElectricityBillCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class ElectricityBillCalculationServiceImpl implements ElectricityBillCalculationService {
    private static final int CLIMATE_CHANGE_CHARGE = 9;
    private static final int FUEL_COST_ADJUSTMENT_CHARGE = 5;
    private static final double VAT = 0.1;
    private static final double ELECTRICITY_INDUSTRY_INFRASTRUCTURE_FUND = 0.037;
    private static final int FEBRUARY = 2;
    private static final int JUNE = 6;
    private static final int AUGUST = 8;
    private static final int NOVEMBER = 11;

    @Override
    public Long calculateElectricityBill(Double kwhUsage, int day) {
        long electricityBill = getGeneralCharge(day)
                + getDemandCharge(kwhUsage)
                + getClimateChangeCharge(kwhUsage)
                + getFuelCostAdjustmentCharge(kwhUsage);

        long billingCharge = electricityBill
                + getVAT(electricityBill)
                + getElectricityIndustryInfraFund(electricityBill);

        return billingCharge;
    }

    @Override
    public Long getGeneralCharge(int day) {
        long generalCharge = SupplyVoltage.HIGH_VOLTAGE_A_OPTION_I.getGeneralCharge();

        return generalCharge * day;
    }

    @Override
    public Long getDemandCharge(Double kwhUsage) {
        int currentMonth = LocalDate.now().getMonthValue();

        double seasonalCharge = 0.0;

        if (currentMonth >= JUNE && currentMonth <= AUGUST) {
            seasonalCharge = DemandCharge.SUMMER.getDemandCharge();
        } else if (currentMonth >= NOVEMBER || currentMonth <= FEBRUARY) {
            seasonalCharge = DemandCharge.WINTER.getDemandCharge();
        } else {
            seasonalCharge = DemandCharge.SPRING_FALL.getDemandCharge();
        }

        return (long) Math.floor(seasonalCharge * kwhUsage);
    }

    @Override
    public Long getClimateChangeCharge(Double kwhUsage) {
        return (long) Math.floor(kwhUsage * CLIMATE_CHANGE_CHARGE);
    }

    @Override
    public Long getFuelCostAdjustmentCharge(Double kwhUsage) {
        return (long) Math.floor(kwhUsage * FUEL_COST_ADJUSTMENT_CHARGE);
    }

    @Override
    public Long getVAT(long electricityBill) {
        return Math.round(electricityBill * VAT);
    }

    @Override
    public Long getElectricityIndustryInfraFund(long electricityBill) {
        return (long) Math.floor(electricityBill * ELECTRICITY_INDUSTRY_INFRASTRUCTURE_FUND);
    }
}
