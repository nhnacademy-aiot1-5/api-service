package live.ioteatime.apiservice.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import live.ioteatime.apiservice.dto.electricity.PreciseElectricityResponseDto;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.HourlyElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HourlyElectricityServiceImpl implements HourlyElectricityService {

    @Value("${spring.influx.bucket}")
    private String bucket;
    @Value("${spring.influx.org}")
    private String organization;
    private final InfluxDBClient influxDBClient;
    private final PlaceRepository placeRepository;

    /**
     * 조직에 소속된 모든 전력센서의 "main" 채널의 시간구간별 전력사용량을 리턴합니다.
     * 시간구간:5분, 단위:kwh (5분동안 총 몇 kwh 를 소비하였는지)
     * 요청시간으로부터 1시간 전까지의 데이터를 리턴합니다.
     *
     * @param organizationId 조직아이디
     * @return 최근 1시간동안의 5분 간격 전체 전력사용량 리스트
     */
    public List<PreciseElectricityResponseDto> getOneHourTotalElectricties(int organizationId) {

        LocalDateTime requestTime = LocalDateTime.now();

        Map<LocalDateTime, Double> totalKwh = new HashMap<>();

        placeRepository.findAllByOrganization_Id(organizationId)
                .forEach(place -> calcFiveMinKwhUsage(place.getPlaceName(), requestTime, totalKwh));

        return totalKwh.entrySet()
                .stream()
                .map(entry -> new PreciseElectricityResponseDto(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private void calcFiveMinKwhUsage(String placeName,LocalDateTime requestTime,
                                         Map<LocalDateTime, Double> totalKwh){

        String fluxQuery = getKwhQuery(placeName, requestTime);

        QueryApi queryApi = influxDBClient.getQueryApi();
        try {
            List<FluxRecord> records = queryApi.query(fluxQuery, organization).get(0).getRecords();
            if(records.isEmpty()) return;

            Map<LocalDateTime, List<Double>> aggregatedData = new TreeMap<>();

            LocalDateTime startTime = requestTime.minusHours(1);
            int timePeriod = 5;
            for(int i=0; i<(60/timePeriod); i++){
                aggregatedData.put(startTime, new ArrayList<>());
                startTime = startTime.plusMinutes(timePeriod);
            }

            LocalDateTime current = requestTime.minusHours(1);
            LocalDateTime next = current.plusMinutes(timePeriod);

            for(FluxRecord r : records){
                LocalDateTime formattedTime = LocalDateTime
                        .parse(Objects.requireNonNull(r.getValueByKey("_time")).toString(), DateTimeFormatter.ISO_DATE_TIME)
                        .plusHours(9);
                Double value = r.getValueByKey("_value") != null ?
                        (Double) Objects.requireNonNull(r.getValueByKey("_value")) : 0.00;

                if (formattedTime.isAfter(next)){
                    aggregatedData.get(current).add(value);
                    current = next;
                    next = current.plusMinutes(timePeriod);
                }
                aggregatedData.get(current).add(value);
            }

            for(Map.Entry<LocalDateTime, List<Double>> entry : aggregatedData.entrySet()) {
                LocalDateTime time = entry.getKey();
                List<Double> values = entry.getValue();

                Double diff = calculateKwhDifference(values);

                if (totalKwh.containsKey(time)) {
                    totalKwh.put(time, totalKwh.get(time) + diff);
                } else {
                    totalKwh.put(time, diff);
                }
            }
        } catch (Exception ignore){
            //
        }
    }

    private Double calculateKwhDifference(List<Double> values){

        if (values.size() < 2) {
            return 0.0;
        }

        Double firstValue = values.get(0);
        Double lastValue = values.get(values.size() - 1);

        return lastValue - firstValue >= 0 ? lastValue - firstValue : 0.0;
    }

    private String getKwhQuery(String placeName, LocalDateTime requestTime){

        LocalDateTime start = requestTime.minusHours(1);

        String startRFC3339 = DateTimeFormatter.ISO_INSTANT.format(start.atZone(ZoneId.systemDefault()).toInstant());
        String endRFC3339 = DateTimeFormatter.ISO_INSTANT.format(requestTime.atZone(ZoneId.systemDefault()).toInstant());

        return "from(bucket: \"" + bucket + "\")\n" +
                "  |> range(start: " + startRFC3339 + ", stop: " + endRFC3339 + ")\n" +
                "  |> filter(fn: (r) => r[\"place\"] == \"" + placeName + "\")\n" +
                "  |> filter(fn: (r) => r[\"phase\"] == \"kwh\")\n" +
                "  |> filter(fn: (r) => r[\"type\"] == \"main\")\n" +
                "  |> filter(fn: (r) => r[\"description\"] == \"sum\")\n" +
                "  |> aggregateWindow(every: 1m, fn: last, createEmpty: false)\n" +
                "  |> yield(name: \"" + placeName + "\")";
    }
}
