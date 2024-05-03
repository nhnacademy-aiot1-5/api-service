package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.ElectricityResponseDto;
import live.ioteatime.apiservice.service.ElectricityService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 일별 전략량을 요청을 응답하는 컨트롤러 입니다.
 */
@RestController
@RequestMapping("/daily")
public class DailyElectricityController {
    private final ElectricityService electricityService;

    /**
     * dailyElectricityService 서비스 빈 의존성 주입을 위한 생성자 입니다.
     * @param electricityService ElectricityService 구현체 DailyElectricityServiceImpl
     */
    public DailyElectricityController(@Qualifier("dailyElectricityService") ElectricityService electricityService) {
        this.electricityService = electricityService;
    }

    /**
     * 일별 전력량을 리스트로 반환합니다. localDateTime이 정각이면 mysql에서 값을 이외 시간은 influxdb에서 최근 24시간 데이터를 리스트로 반환합니다.
     *
     * @param localDateTime 요청 할 기준 시간입니다.
     * @param channelId     상세 위치
     * @return 시간과 전력량을 가진 응답 DTO 리스트
     */
    @GetMapping("/electricities")
    public ResponseEntity<List<ElectricityResponseDto>> getDailyElectricities(
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            @RequestParam LocalDateTime localDateTime,
            @RequestParam int channelId) {
        return ResponseEntity.ok(electricityService.getElectricitiesByDate(
                new ElectricityRequestDto(localDateTime, channelId)));
    }
}
