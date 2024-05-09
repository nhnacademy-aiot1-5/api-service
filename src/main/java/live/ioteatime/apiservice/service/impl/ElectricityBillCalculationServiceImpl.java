package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.DemandCharge;
import live.ioteatime.apiservice.domain.SupplyVoltage;
import live.ioteatime.apiservice.service.ElectricityBillCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ElectricityBillCalculationServiceImpl implements ElectricityBillCalculationService {
    @Override
    public Long calculateElectricityBill(Long kwh) {
        // 전력요금(electricityBill) = 기본요금 + 전력량요금 + 기후변화요금 + 연료비조정요금
        long electricityBill = getDemandCharge(kwh) + getDemandCharge(kwh) + getClimateChangeCharge(kwh) + getFuelCostAdjustmentCharge(kwh);

        // 청구요금(billingCharge) = 전력요금 + 부가가치세 + 전력산업기반기금
        long billingCharge = electricityBill + getVAT(electricityBill) + getElectricityIndustryInfraFund(electricityBill);

        return billingCharge;
    }


    @Override
    public Long getGeneralCharge() {
        // 고압 A 옵션 I의 기본 요금을 가져옵니다.
        long generalCharge = SupplyVoltage.HIGH_VOLTAGE_A_OPTION_I.getGeneralCharge();

        return generalCharge * 30L; // 30일치 기본 요금을 반환합니다.
    }


    @Override
    public Long getDemandCharge(Long kwh) {
        int currentMonth = LocalDateTime.now().getMonthValue();

        double seasonalCharge = 0;

        if (currentMonth >= 6 && currentMonth <= 8) {
            seasonalCharge = DemandCharge.SUMMER.getDemandCharge();
        } else if (currentMonth <= 2 || currentMonth >= 11) {
            seasonalCharge = DemandCharge.WINTER.getDemandCharge();
        } else {
            seasonalCharge = DemandCharge.SPRING_FALL.getDemandCharge();
        }

        return (long) Math.floor(seasonalCharge * kwh); // 사용한 전력만큼 계절 요금을 곱해서 반환합니다.
    }

    @Override
    public Long getClimateChangeCharge(Long kwh) {
        return (long) Math.floor(kwh * 9); // 기후변화요금 9원에 사용한 전력을 곱해서 반환합니다.
    }

    @Override
    public Long getFuelCostAdjustmentCharge(Long kwh) {
        return (long) Math.floor(kwh * 5); // 연료비조정요금 5원에 사용한 전력을 곱해서 반환합니다.
    }

    @Override
    public Long getVAT(Long electricityBill) {
        return Math.round(electricityBill * 0.1); // 전력요금의 10%를 부가가치세로 반환합니다.
    }

    @Override
    public Long getElectricityIndustryInfraFund(Long electricityBill) {
        return (long) Math.floor(electricityBill * 0.037); // 전력요금의 3.7%를 반환합니다.
    }
}
