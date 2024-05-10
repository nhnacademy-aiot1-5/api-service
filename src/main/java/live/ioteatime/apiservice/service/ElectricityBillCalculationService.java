package live.ioteatime.apiservice.service;

public interface ElectricityBillCalculationService {

    /**
     * 전력요금을 계산하고 청구요금을 반환하는 메서드입니다.
     * <br>
     * 요금 계산 결과 소수점 이하는 버림 처리합니다.
     * <br>
     * 전력요금(electricityBill) = 기본요금 + 전력량요금 + 기후변화요금 + 연료비조정요금
     * <br>
     * 청구요금(billingCharge) = 전력요금 + 부가가치세 + 전력산업기반기금
     * <br>
     * @return 청구요금(billingCharge)을 반환합니다.
     */
    Long calculateElectricityBill(Long kwh);

    /**
     * 기본요금을 반환하는 메서드입니다.
     * @return 30일치 기본요금을 반환합니다.
     */
    Long getGeneralCharge();

    /**
     * 전력량요금(사용한 전력에 대한 요금)을 반환하는 메서드입니다.
     * @return 30일치 전력량요금을 반환합니다.
     */
    Long getDemandCharge(Long kwh);

    /**
     * 기후변화요금을 반환하는 메서드입니다.
     * @return 기후변화요금을 반환합니다.
     */
    Long getClimateChangeCharge(Long kwh);

    /**
     * 연료비조정요금을 반환하는 메서드입니다.
     * @return 연료비조정요금을 반환합니다.
     */
    Long getFuelCostAdjustmentCharge(Long kwh);

    /**
     * 전력요금의 10%인 부가가치세를 반환하는 메서드입니다.
     * @return 부가가치세를 반환합니다.
     */
    Long getVAT(Long electricityBill);

    /**
     * 전력요금의 3.7%인 전력산업발전기금을 반환하는 메서드입니다.
     * @return 전력산업기반기금을 반환합니다.
     */
    Long getElectricityIndustryInfraFund(Long electricityBill);
}
