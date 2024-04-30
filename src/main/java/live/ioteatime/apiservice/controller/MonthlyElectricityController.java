package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.domain.MonthlyElectricity;
import live.ioteatime.apiservice.dto.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.ElectricityResponseDto;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/monthly")
@RequiredArgsConstructor
public class MonthlyElectricityController {
    private final ElectricityService<MonthlyElectricity> electricityService;

    @GetMapping("/electricity")
    public ElectricityResponseDto getMonthlyElectricity(@RequestParam
                                                        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                        LocalDateTime localDateTime,
                                                        @RequestParam int channelId) {
        ElectricityRequestDto requestDto = new ElectricityRequestDto(localDateTime, channelId);
        MonthlyElectricity monthlyElectricity = electricityService.getElectricityByDate(requestDto);

        return new ElectricityResponseDto(monthlyElectricity.getPk().getTime(), monthlyElectricity.getKwh());
    }

    @GetMapping("/electricities")
    public List<ElectricityResponseDto> getMonthlyElectricies(@RequestParam
                                                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                              LocalDateTime localDateTime,
                                                              @RequestParam int channelId) {
        return electricityService.getElectricitiesByDate(new ElectricityRequestDto(localDateTime, channelId));
    }
}
