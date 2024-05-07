package live.ioteatime.apiservice.controller;

import live.ioteatime.apiservice.dto.electricity.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/monthly")
public class MonthlyElectricityController {
    private final ElectricityService electricityService;

    /**
     * MonthlyElectricityService 서비스 빈 의존성 주입을 위한 생성자 입니다.
     * @param electricityService ElectricityService 구현체 MonthlyElectricityServiceImpl
     */
    public MonthlyElectricityController(@Qualifier("monthlyElectricityService") ElectricityService electricityService) {
        this.electricityService = electricityService;
    }

    /**
     * 지정된 날짜와 채널 ID에 해당하는 월별 전력 사용량을 조회합니다.
     *
     * @param localDateTime 조회하고자 하는 날짜와 시간. ISO 날짜-시간 형식을 따릅니다.
     * @param channelId     조회하고자 하는 채널의 ID.
     * @return 조회된 월별 전력 사용량을 {@link ElectricityResponseDto} 객체로 반환합니다.
     */
    @GetMapping("/electricity")
    public ResponseEntity<ElectricityResponseDto> getMonthlyElectricity(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                        @RequestParam LocalDateTime localDateTime,
                                                                        @RequestParam int channelId) {
        return ResponseEntity.ok(electricityService.getElectricityByDate(
                new ElectricityRequestDto(localDateTime, channelId)));
    }

    /**
     * 지정된 날짜와 채널 ID에 해당하는 모든 월별 전력 사용량 데이터를 조회합니다.
     *
     * @param localDateTime 조회하고자 하는 날짜와 시간. ISO 날짜-시간 형식을 따릅니다.
     * @param channelId     조회하고자 하는 채널의 ID.
     * @return 조회된 모든 월별 전력 사용량 데이터를 {@link ElectricityResponseDto} 객체의 리스트로 반환합니다.
     */
    @GetMapping("/electricities")
    public ResponseEntity<List<ElectricityResponseDto>> getMonthlyElectricies(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                              @RequestParam LocalDateTime localDateTime,
                                                                              @RequestParam int channelId) {
        return ResponseEntity.ok(electricityService.getElectricitiesByDate(
                new ElectricityRequestDto(localDateTime, channelId)));
    }
}
