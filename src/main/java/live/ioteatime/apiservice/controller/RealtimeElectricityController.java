package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.electricity.RealtimeElectricityResponseDto;
import live.ioteatime.apiservice.service.RealtimeElectricityService;
import lombok.RequiredArgsConstructor;
import live.ioteatime.apiservice.dto.electricity.KwhResponseDto;
import live.ioteatime.apiservice.service.impl.DailyElectricityServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/realtime")
@RequiredArgsConstructor
public class RealtimeElectricityController {

    private final DailyElectricityServiceImpl electricityService; // 나
    private final RealtimeElectricityService realtimeElectricityService; // 은지


    @GetMapping("/total")
    public ResponseEntity<List<KwhResponseDto>> getRealTimeTotalElectricities(@RequestParam int organizationId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(electricityService.getRealtimeTotalElectricties(organizationId));
    }

    @GetMapping("/electricity")
    public ResponseEntity<List<RealtimeElectricityResponseDto>> getRealtimeElectricity(@RequestParam int organizationId){
        return ResponseEntity.ok(realtimeElectricityService.getRealtimeElectricity(organizationId));
    }
}
