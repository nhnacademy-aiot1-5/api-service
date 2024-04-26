package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.domain.HourlyElectricity;
import live.ioteatime.apiservice.dto.ElectricityResponseDto;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/hourly")
public class HourlyElectricityController {
    private final ElectricityService<HourlyElectricity> electricityService;

    @GetMapping("/electricities")
    public List<ElectricityResponseDto> getHourlyElectricities() {
//        electricityService.getElectricitiesByDate()
        return null;
    }
}
