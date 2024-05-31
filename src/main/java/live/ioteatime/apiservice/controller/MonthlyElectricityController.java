package live.ioteatime.apiservice.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Monthly Electricity", description = "월별 전력량 API")
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
    @Operation(summary = "채널의 금월 전력량 조회", description = "해당 채널의 요청월의 전력량을 조회합니다.")
    public ResponseEntity<ElectricityResponseDto> getMonthlyElectricity(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                        @RequestParam LocalDateTime localDateTime,
                                                                        @RequestParam int channelId) {
        return ResponseEntity.ok(electricityService.getElectricityByDate(
                new ElectricityRequestDto(localDateTime, channelId)));
    }

    @GetMapping("/electricity/last")
    @Operation(summary = "전월 총전력량 조회", description = "전월의 모든 채널의 총전력량을 조회합니다.")
    public ResponseEntity<ElectricityResponseDto> getLastMonthElectricity(){
        return ResponseEntity.ok(electricityService.getLastElectricity());
    }

    @GetMapping("/electricity/current")
    @Operation(summary = "금월 총전력량 조회", description = "금월의 모든 채널의 총전력량을 조회합니다.")
    public ResponseEntity<ElectricityResponseDto> getcurrentMonthElectricity(){
        return ResponseEntity.ok(electricityService.getCurrentElectricity());
    }

    /**
     * 지정된 날짜와 채널 ID에 해당하는 모든 월별 전력 사용량 데이터를 조회합니다.
     *
     * @param localDateTime 조회하고자 하는 날짜와 시간. ISO 날짜-시간 형식을 따릅니다.
     * @param channelId     조회하고자 하는 채널의 ID.
     * @return 조회된 모든 월별 전력 사용량 데이터를 {@link ElectricityResponseDto} 객체의 리스트로 반환합니다.
     */
    @GetMapping("/electricities")
    @Operation(summary = "최근 1년 간 월별 전력량 조회", description = "해당 채널의 최근 1년 간 월별 전력량을 조회합니다.")
    public ResponseEntity<List<ElectricityResponseDto>> getMonthlyElectricties(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                                                                              @RequestParam LocalDateTime localDateTime,
                                                                              @RequestParam int channelId) {
        return ResponseEntity.ok(electricityService.getElectricitiesByDate(
                new ElectricityRequestDto(localDateTime, channelId)));
    }
}
