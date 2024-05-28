package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.electricity.DemandChargeResponse;
import live.ioteatime.apiservice.dto.electricity.ElectricityBillResponse;
import live.ioteatime.apiservice.service.ElectricityBillCalculationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/calculate")
public class ElectricityBillCalculationController {
    private final ElectricityBillCalculationService electricityBillCalculationService;

    @GetMapping("/demand/{kwhUsage}")
    public ResponseEntity<DemandChargeResponse> calculateDemandCharge(@PathVariable Double kwhUsage) {

        DemandChargeResponse response = DemandChargeResponse.builder()
                .demandCharge(electricityBillCalculationService.getDemandCharge(kwhUsage))
                .build();

        return ResponseEntity.ok(response);
    }

    @GetMapping("/bill")
    public ResponseEntity<ElectricityBillResponse> calculateElectricityBill(
            @RequestParam("kwhUsage") Double kwhUsage,
            @RequestParam("day") int day) {
        ElectricityBillResponse response = ElectricityBillResponse.builder()
                .electricityBill(electricityBillCalculationService.calculateElectricityBill(kwhUsage, day))
                .build();

        return ResponseEntity.ok(response);
    }
}