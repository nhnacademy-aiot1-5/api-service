package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.annotation.VerifyOrganization;
import live.ioteatime.apiservice.dto.electricity.RealtimeElectricityResponseDto;
import live.ioteatime.apiservice.service.RealtimeElectricityService;
import lombok.RequiredArgsConstructor;
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

    private final RealtimeElectricityService realtimeElectricityService;

    @VerifyOrganization
    @GetMapping("/electricity")
    public ResponseEntity<List<RealtimeElectricityResponseDto>> getRealtimeElectricity(@RequestParam int organizationId){
        return ResponseEntity.ok(realtimeElectricityService.getRealtimeElectricity(organizationId));
    }
}
