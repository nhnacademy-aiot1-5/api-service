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
    public Long calculateElectricityBill(Double thisMonthKwhUsage) {
        long electricityBill = getGeneralCharge()
                + getDemandCharge(thisMonthKwhUsage)
                + getClimateChangeCharge(thisMonthKwhUsage)
                + getFuelCostAdjustmentCharge(thisMonthKwhUsage);

        long billingCharge = electricityBill
                + getVAT(electricityBill)
                + getElectricityIndustryInfraFund(electricityBill);

        return billingCharge;
    }

    @Override
    public Long getGeneralCharge() {
        long generalCharge = SupplyVoltage.HIGH_VOLTAGE_A_OPTION_I.getGeneralCharge();

        int THIS_MONTH_DAYS = LocalDate.now().lengthOfMonth();

        return generalCharge * THIS_MONTH_DAYS;
    }


    @Override
    public Long getDemandCharge(Double thisMonthKwhUsage) {
        int currentMonth = LocalDate.now().getMonthValue();

        double seasonalCharge = 0.0;

        if (currentMonth >= JUNE && currentMonth <= AUGUST) {
            seasonalCharge = DemandCharge.SUMMER.getDemandCharge();
        } else if (currentMonth >= NOVEMBER || currentMonth <= FEBRUARY) {
            seasonalCharge = DemandCharge.WINTER.getDemandCharge();
        } else {
            seasonalCharge = DemandCharge.SPRING_FALL.getDemandCharge();
        }

        return (long) Math.floor(seasonalCharge * thisMonthKwhUsage);
    }

    @Override
    public Long getClimateChangeCharge(Double thisMonthKwhUsage) {
        return (long) Math.floor(thisMonthKwhUsage * CLIMATE_CHANGE_CHARGE);
    }

    @Override
    public Long getFuelCostAdjustmentCharge(Double thisMonthKwhUsage) {
        return (long) Math.floor(thisMonthKwhUsage * FUEL_COST_ADJUSTMENT_CHARGE);
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
