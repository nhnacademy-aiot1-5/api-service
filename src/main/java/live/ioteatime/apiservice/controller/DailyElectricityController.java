package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.domain.DailyElectricity;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/daily")
public class DailyElectricityController {
    private final ElectricityService<DailyElectricity> electricityService;

    @GetMapping("/electricities")
    public List<DailyElectricity> getDailyElectricities(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        return electricityService.getElectricitiesByDate(localDate);
    }
}
