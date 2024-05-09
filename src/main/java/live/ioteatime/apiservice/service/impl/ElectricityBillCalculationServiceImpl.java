package live.ioteatime.apiservice.service.impl;

import live.ioteatime.apiservice.domain.DemandCharge;
import live.ioteatime.apiservice.domain.SupplyVoltage;
import live.ioteatime.apiservice.service.ElectricityBillCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

/*
*    일반용(갑) 전력 요금을 계산하기 위한 서비스입니다.
*/

@Service
@RequiredArgsConstructor
public class ElectricityBillCalculationServiceImpl implements ElectricityBillCalculationService {

    /*
    * 전력요금을 계산하고 청구요금을 반환하는 메서드입니다.
    * 요금 계산 중 소수점 이하는 버림 처리합니다.
    *
    * 전력요금(electricityBill) = 기본요금 + 전력량요금 + 기후변화요금 + 연료비조정요금
    *
    * 청구요금(billingCharge) = 전력요금 + 부가가치세 + 전력산업기반기금
    */

    @Override
    public Long calculateElectricityBill(Long kwh) {
        long electricityBill = getDemandCharge() + getDemandCharge() + getClimateChangeCharge(kwh) + getFuelCostAdjustmentCharge(kwh);

        long billingCharge = electricityBill + getVAT(electricityBill) + getElectricityIndustryInfraFund(electricityBill);

        return billingCharge;
    }

    /*
    * 기본요금을 반환하는 메서드입니다.
    */

    @Override
    public Long getGeneralCharge() {
        long generalCharge = SupplyVoltage.HIGH_VOLTAGE_A_OPTION_I.getGeneralCharge();

        return generalCharge * 30L;
    }

    /*
    * 전력량요금(사용한 전력에 대한 요금)을 반환하는 메서드입니다.
    */

    @Override
    public Long getDemandCharge() {
        int currentMonth = LocalDateTime.now().getMonthValue();

        double chargePerMonth = 0;

        if (currentMonth >= 6 && currentMonth <= 8) {
            chargePerMonth = DemandCharge.SUMMER.getDemandCharge();
        } else if (currentMonth <= 2 || currentMonth >= 11) {
            chargePerMonth = DemandCharge.WINTER.getDemandCharge();
        } else {
            chargePerMonth = DemandCharge.SPRING_FALL.getDemandCharge();
        }

        return (long) Math.floor(chargePerMonth * 30);
    }

    /*
    * 기후변화요금을 반환하는 메서드입니다.
    */

    @Override
    public Long getClimateChangeCharge(Long kwh) {
        return (long) Math.floor(kwh * 9);
    }

    /*
    * 연료비조정요금을 반환하는 메서드입니다.
    */

    @Override
    public Long getFuelCostAdjustmentCharge(Long kwh) {
        return (long) Math.floor(kwh * 5);
    }

    /*
    * 부가가치세를 반환하는 메서드입니다.
    */

    @Override
    public Long getVAT(Long electricityBill) {
        return Math.round(electricityBill * 0.1);
    }

    /*
    * 전력산업기반기금을 반환하는 메서드입니다.
    */

    @Override
    public Long getElectricityIndustryInfraFund(Long electricityBill) {
        return (long) Math.floor(electricityBill * 0.037);
    }
}
