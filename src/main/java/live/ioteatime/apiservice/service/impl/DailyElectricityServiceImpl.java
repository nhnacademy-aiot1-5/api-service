package live.ioteatime.apiservice.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import live.ioteatime.apiservice.domain.DailyElectricity;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.ElectricityRequestDto;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.DailyElectricityRepository;
import live.ioteatime.apiservice.repository.OrganizationRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class DailyElectricityServiceImpl implements ElectricityService<DailyElectricity> {
    @Value("${spring.influx.bucket}")
    private String bucket;
    @Value("${spring.influx.org}")
    private String organization;
    private final DailyElectricityRepository dailyElectricityRepository;
    private final InfluxDBClient influxDBClient;
    private final OrganizationRepository organizationRepository;
    private final PlaceRepository placeRepository;

    @Override
    public DailyElectricity getElectricityByDate(ElectricityRequestDto electricityRequestDto) {
        DailyElectricity.Pk pk = new DailyElectricity.Pk(electricityRequestDto.getOrganizationId(), electricityRequestDto.getTime());
        return dailyElectricityRepository.findByPk(pk)
                .orElseThrow(() -> new ElectricityNotFoundException("Daily electricity not found for " + pk));
    }

    @Override
    public List<DailyElectricity> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        if (electricityRequestDto.getTime().toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return getDailyElectricitiesByDate(electricityRequestDto);
        } else {
            return getHourlyElectricitiesByDate(electricityRequestDto);
        }
    }

    // mysql에서 한달 치 일별 데이터 가져오기
    private List<DailyElectricity> getDailyElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        LocalDateTime localDateTime = electricityRequestDto.getTime();
        LocalDateTime startOfMonth = localDateTime.withDayOfMonth(1);
        LocalDateTime endOfMonth = localDateTime.withDayOfMonth(localDateTime.toLocalDate().lengthOfMonth());
        log.warn("start month : " + startOfMonth + ", end month : " + endOfMonth);
        return dailyElectricityRepository.findAllByPkOrganizationIdAndPkTimeBetween(
                electricityRequestDto.getOrganizationId(),
                startOfMonth,
                endOfMonth
        );
    }

    // influxdb에서 금일 시간 별 데이터 가져오기
    private List<DailyElectricity> getHourlyElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        LocalDateTime localDateTime = electricityRequestDto.getTime();
        LocalDateTime startTimeOfDay = localDateTime.minusDays(1).plusHours(1);
        log.warn("start time : " + startTimeOfDay + ", end time : " + localDateTime);
        return fetchFromInfluxDB(electricityRequestDto.getOrganizationId(), startTimeOfDay, localDateTime);
    }

    /*
    place | type
    전체   | 전체 -> office, classA 합한 전력량
    classa| ac ->
    office| ac ->
    classa| temperature ->
     */
    private List<DailyElectricity> fetchFromInfluxDB(String place, String type, int organizationId, LocalDateTime start,
                                                     LocalDateTime end) {
        String fluxQuery = getQuery(place, type, organizationId, start, end);

        Map<LocalDateTime, DailyElectricity> results = new TreeMap<>();
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, organization);

        for (FluxTable table : tables) {
            for (FluxRecord r : table.getRecords()) {

                LocalDateTime formattedTime = LocalDateTime
                        .parse(Objects.requireNonNull(r.getValueByKey("_time")).toString(), DateTimeFormatter.ISO_DATE_TIME)
                        .plusHours(9);
                Long value = r.getValueByKey("_value") != null ? ((Double) Objects.requireNonNull(r.getValueByKey("_value"))).longValue() : 0L;
                results.merge(formattedTime, new DailyElectricity(
                        new DailyElectricity.Pk(organizationId, formattedTime),
                        organizationRepository.findById(organizationId).orElse(null),
                        value,
                        0
                ), (existing, newEntry) -> new DailyElectricity(
                        existing.getPk(),
                        existing.getOrganization(),
                        existing.getKwh() + newEntry.getKwh()
                ));
            }
        }
        return new ArrayList<>(results.values());
    }

    private String getQuery(String place, String type, int organizationId, LocalDateTime start, LocalDateTime end) {
        String startRFC3339 = DateTimeFormatter.ISO_INSTANT.format(start.atZone(ZoneId.systemDefault()).toInstant());
        String endRFC3339 = DateTimeFormatter.ISO_INSTANT.format(end.atZone(ZoneId.systemDefault()).toInstant());
        StringBuilder fluxQuery = new StringBuilder(
                "from(bucket: \""
//                        "  |> range(start: %s, stop: %s)\n"
//                        "  |> filter(fn: (r) => r[\"place\"] == \"office\" or r[\"place\"] == \"class_a\")\n" +
//                        "  |> filter(fn: (r) => r[\"type\"] == \"main\")\n" +
//                        "  |> filter(fn: (r) => r[\"phase\"] == \"total\")\n" +
//                        "  |> filter(fn: (r) => r[\"description\"] == \"w\")\n" +
//                        "  |> aggregateWindow(every: 1h, fn: mean, createEmpty: false)\n" +
//                        "  |> yield(name: \"mean\")",
//                bucket, startRFC3339, endRFC3339
        );
        fluxQuery.append(bucket).append("\")\n")
                .append("  |> range(start: ").append(startRFC3339).append(", stop: ").append(endRFC3339).append("\n")
                .append("  |> filter(fn: (r) => ");
        List<Place> places = placeRepository.findAllByOrganization_Id(organizationId);

        if (place.equals("total") && places.size() > 1) {
            Iterator<Place> iterator = places.iterator();
            while (iterator.hasNext()) {
                Place p = iterator.next();
                fluxQuery.append("r[\"place\"] == \"")
                        .append(p.getPlaceName())
                        .append("\"");
                if (iterator.hasNext()) {
                    fluxQuery.append(" or ");
                }
            }
        } else {
            fluxQuery.append("r[\"place\"] == \"")
                    .append(places.stream().findFirst().orElseThrow(EntityNotFoundException::new).getPlaceName())
                    .append("\"");
        }
        fluxQuery.append(")\n")
                .append("  |> filter(fn: (r) => r[\"type\"] == \"")
                .append(type)
                .append("\")\n")
                .append("  |> filter(fn: (r) => r[\"phase\"] == \"total\")\n")
                .append("  |> filter(fn: (r) => r[\"description\"] == \"w\")\n")
                .append("  |> aggregateWindow(every: 1h, fn: mean, createEmpty: false)\n")
                .append("  |> yield(name: \"mean\")");

        return fluxQuery.toString();
    }
}
