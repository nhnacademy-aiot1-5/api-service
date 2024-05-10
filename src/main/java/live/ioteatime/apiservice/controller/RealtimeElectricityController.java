package live.ioteatime.apiservice.controller;

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
@RequestMapping("/realtime-total")
@RequiredArgsConstructor
public class RealtimeElectricityController {

    private final DailyElectricityServiceImpl electricityService;

    @GetMapping
    public ResponseEntity<List<KwhResponseDto>> getRealTimeTotalElectricities(@RequestParam int organizationId){

        return ResponseEntity.status(HttpStatus.OK)
                .body(electricityService.getRealtimeTotalElectricties(organizationId));
    }


}
