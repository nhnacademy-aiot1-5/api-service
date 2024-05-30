package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import live.ioteatime.apiservice.dto.electricity.DemandChargeResponse;
import live.ioteatime.apiservice.dto.electricity.ElectricityBillResponse;
import live.ioteatime.apiservice.service.ElectricityBillCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculate")
@Tag(name = "Electricity Bill Calculation", description = "전기 요금 계산 API")
public class ElectricityBillCalculationController {
    private final ElectricityBillCalculationService electricityBillCalculationService;

    @GetMapping("/demand/{kwhUsage}")
    @Operation(summary = "전력량 요금 조회", description = "사용한 전력 소비량에 대한 요금을 계산하여 반환합니다.")
    public ResponseEntity<DemandChargeResponse> calculateDemandCharge(@PathVariable Double kwhUsage) {

        DemandChargeResponse response = DemandChargeResponse.builder()
                .demandCharge(electricityBillCalculationService.getDemandCharge(kwhUsage))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/bill")
    @Operation(summary = "청구 요금 조회", description = "부가가치세, 전력산업기반기금, 연료비조정요금, 기후환경변화요금을 포함한 청구요금을 계산하여 반환합니다.")
    public ResponseEntity<ElectricityBillResponse> calculateElectricityBill(
            @RequestParam("kwhUsage") Double kwhUsage,
            @RequestParam("day") int day) {
        ElectricityBillResponse response = ElectricityBillResponse.builder()
                .electricityBill(electricityBillCalculationService.calculateElectricityBill(kwhUsage, day))
                .build();

        return ResponseEntity.ok(response);
    }
}