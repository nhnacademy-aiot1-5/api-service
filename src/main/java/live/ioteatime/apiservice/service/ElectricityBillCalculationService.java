package live.ioteatime.apiservice.service;

public interface ElectricityBillCalculationService {
    Long calculateElectricityBill(Long kwh);

    Long getGeneralCharge();

    Long getDemandCharge();

    Long getClimateChangeCharge(Long kwh);

    Long getFuelCostAdjustmentCharge(Long kwh);

    Long getVAT(Long electricityBill);

    Long getElectricityIndustryInfraFund(Long electricityBill);
}
