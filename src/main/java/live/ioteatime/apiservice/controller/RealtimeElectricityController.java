package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Realtime Electricity", description = "실시간 전력량 API")
public class RealtimeElectricityController {

    private final RealtimeElectricityService realtimeElectricityService;

    @VerifyOrganization
    @GetMapping("/electricity")
    @Operation(summary = "채널별 실시간 전력량 조회", description = "조직의 채널별 실시간 전력량을 조회합니다.")
    public ResponseEntity<List<RealtimeElectricityResponseDto>> getRealtimeElectricity(@RequestParam int organizationId){
        return ResponseEntity.ok(realtimeElectricityService.getRealtimeElectricity(organizationId));
    }
}
