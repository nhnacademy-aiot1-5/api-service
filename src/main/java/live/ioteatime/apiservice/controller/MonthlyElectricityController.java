package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.domain.MonthlyElectricity;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@Slf4j
@RestController
@RequestMapping("/monthly")
@RequiredArgsConstructor
public class MonthlyElectricityController {
    private final ElectricityService<MonthlyElectricity> electricityService;

    @GetMapping("/electricity")
    public MonthlyElectricity getMonthlyElectricity(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate localDate) {
        return electricityService.getElectricityByDate(localDate);
    }
}
