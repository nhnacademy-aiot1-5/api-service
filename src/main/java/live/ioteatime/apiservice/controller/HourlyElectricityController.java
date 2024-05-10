package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import live.ioteatime.apiservice.dto.electricity.PreciseKwhResponseDto;
import live.ioteatime.apiservice.service.HourlyElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/hourly")
@RequiredArgsConstructor
public class HourlyElectricityController {

    private final HourlyElectricityService hourlyElectricityService;


    @GetMapping("/electricities/total")
    @Operation(summary = "최근 1시간 kwh 사용량을 5분간격으로 계산하여 가져오는 API", description = "5분동안 몇kwh를 소비하였는지, 총 12개의 데이터를 반환합니다.")
    public ResponseEntity<List<PreciseKwhResponseDto>> getOneHourTotalElectricties(@RequestParam int organizationId){
        return ResponseEntity.status(HttpStatus.OK)
                .body(hourlyElectricityService.getOneHourTotalElectricties(organizationId));
    }

}
