package live.ioteatime.apiservice.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.DailyElectricity;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.electricity.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.ChannelRepository;
import live.ioteatime.apiservice.repository.DailyElectricityRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service("dailyElectricityService")
@RequiredArgsConstructor
public class DailyElectricityServiceImpl implements ElectricityService {
    @Value("${spring.influx.bucket}")
    private String bucket;
    @Value("${spring.influx.org}")
    private String organization;
    private final DailyElectricityRepository dailyElectricityRepository;
    private final InfluxDBClient influxDBClient;
    private final PlaceRepository placeRepository;
    private final ChannelRepository channelRepository;

    @Override
    public ElectricityResponseDto getElectricityByDate(ElectricityRequestDto electricityRequestDto) {
        DailyElectricity.Pk pk = new DailyElectricity.Pk(electricityRequestDto.getChannelId(), electricityRequestDto.getTime());

        DailyElectricity dailyElectricity = dailyElectricityRepository.findByPk(pk)
                .orElseThrow(() -> new ElectricityNotFoundException("Daily electricity not found for " + pk));
        return new ElectricityResponseDto(dailyElectricity.getPk().getTime(), dailyElectricity.getKwh(), dailyElectricity.getBill());
    }

    @Override
    @Transactional
    public List<ElectricityResponseDto> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        if (electricityRequestDto.getTime().toLocalTime().equals(LocalTime.MIDNIGHT)) {
            return getDailyElectricitiesByDate(electricityRequestDto);
        } else {
            return getHourlyElectricitiesByDate(electricityRequestDto);
        }
    }

    @Override
    public ElectricityResponseDto getCurrentElectricity() {
        LocalDateTime start = LocalDateTime.now().minusDays(1).plusSeconds(1);
        LocalDateTime end = LocalDateTime.now();

        String fluxQuery = getKwhQuery("total", "main", start, end);
        QueryApi queryApi = influxDBClient.getQueryApi();
        FluxTable fluxTable = queryApi.query(fluxQuery, organization).get(0);
        List<FluxRecord> records = fluxTable.getRecords();

        long result = 0;
        if (!records.isEmpty()) {
            FluxRecord firstRecord = records.get(0);
            FluxRecord lastRecord = records.get(records.size() - 1);

            Double firstKwh = (Double) firstRecord.getValueByKey("_value");
            Double lastKwh = (Double) lastRecord.getValueByKey("_value");

            result = (long) (lastKwh - firstKwh);
        }

        return new ElectricityResponseDto(end, result, 0L);
    }

    @Override
    public ElectricityResponseDto getLastElectricity() {
        List<Channel> channels = channelRepository.findAllByChannelName("main");
        LocalDateTime endOfDay = LocalDateTime.now().minusDays(1).toLocalDate().atStartOfDay();

        long kwh = 0;
        for (Channel channel : channels) {
            int channelId = channel.getId();
            DailyElectricity.Pk pk = new DailyElectricity.Pk(channelId, endOfDay);
            Optional<DailyElectricity> dailyElectricity = dailyElectricityRepository.findByPk(pk);
            if (dailyElectricity.isPresent()) {
                kwh += dailyElectricity.get().getKwh();
            }
        }
        return new ElectricityResponseDto(endOfDay, kwh, 0L);
    }

    @Override
    public List<ElectricityResponseDto> getTotalElectricitiesByDate(LocalDateTime localDateTime, int organizationId) {
        LocalDateTime start = localDateTime.withDayOfMonth(1).withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = localDateTime.withHour(0).withMinute(0).withSecond(0).withNano(0);

        Map<LocalDateTime, ElectricityResponseDto> totalKwh = new HashMap<>();
        for (LocalDateTime date = start; !date.isAfter(end); date = date.plusDays(1)) {
            totalKwh.put(date, new ElectricityResponseDto(date, 0L, null));
        }

        List<Place> placeList = placeRepository.findAllByOrganization_Id(organizationId);
        for (Place place : placeList) {
            Channel channel = channelRepository.findByPlaceAndChannelName(place, "main");
            if (channel == null) {
                continue;
            }
            List<DailyElectricity> currentMonthDataList = dailyElectricityRepository
                    .findAllByPkChannelIdAndPkTimeBetween(channel.getId(), start, end);
            for (DailyElectricity data : currentMonthDataList) {
                LocalDateTime key = data.getPk().getTime();
                long kwh = totalKwh.get(key).getKwh();
                totalKwh.get(key).setKwh(kwh + data.getKwh());
            }
        }
        return new ArrayList<>(totalKwh.values());
    }

    // mysql에서 2달 치 일별 데이터 가져오기 이유는 월 초에는 최근 1주일치를 가져올 수 없음
    private List<ElectricityResponseDto> getDailyElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        LocalDateTime localDateTime = electricityRequestDto.getTime();
        LocalDateTime startOfMonth = localDateTime.withDayOfMonth(1).minusDays(1).withDayOfMonth(1);
        LocalDateTime endOfMonth = localDateTime.withDayOfMonth(localDateTime.toLocalDate().lengthOfMonth());
        List<DailyElectricity> dailyElectricities = dailyElectricityRepository.findAllByPkChannelIdAndPkTimeBetween(
                electricityRequestDto.getChannelId(),
                startOfMonth,
                endOfMonth
        );
        List<ElectricityResponseDto> electricityResponseDtos = new ArrayList<>();

        for (DailyElectricity dailyElectricity : dailyElectricities) {
            electricityResponseDtos.add(
                    new ElectricityResponseDto(dailyElectricity.getPk().getTime(), dailyElectricity.getKwh(), dailyElectricity.getBill())
            );
        }
        return electricityResponseDtos;
    }

    // influxdb에서 금일 시간 별 데이터 가져오기
    public List<ElectricityResponseDto> getHourlyElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        LocalDateTime localDateTime = electricityRequestDto.getTime();
        Channel channel = channelRepository.findById(electricityRequestDto.getChannelId())
                .orElseThrow(EntityNotFoundException::new);
        Place place = placeRepository.findById(channel.getPlace().getId())
                .orElseThrow(EntityNotFoundException::new);

        List<DailyElectricity> dailyElectricities = fetchFromInfluxDB(
                place.getPlaceName(),
                channel.getChannelName(),
                place.getOrganization().getId(),
                channel.getId(),
                localDateTime.minusDays(1).plusHours(1),
                localDateTime
        );
        List<ElectricityResponseDto> electricityResponseDtos = new ArrayList<>();

        for (DailyElectricity dailyElectricity : dailyElectricities) {
            electricityResponseDtos.add(
                    new ElectricityResponseDto(
                            dailyElectricity.getPk().getTime(),
                            dailyElectricity.getKwh(),
                            dailyElectricity.getBill())
            );
        }
        return electricityResponseDtos;
    }

    private List<DailyElectricity> fetchFromInfluxDB(String place, String type, int organizationId, int channelId,
                                                     LocalDateTime start, LocalDateTime end) {
        String fluxQuery = getQuery(place, type, organizationId, start, end);
        Map<LocalDateTime, DailyElectricity> results = new TreeMap<>();
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(fluxQuery, organization);

        for (FluxTable table : tables) {
            for (FluxRecord r : table.getRecords()) {
                LocalDateTime formattedTime = LocalDateTime
                        .parse(Objects.requireNonNull(r.getValueByKey("_time")).toString(), DateTimeFormatter.ISO_DATE_TIME)
                        .plusHours(9);
                Long value = r.getValueByKey("_value") != null ?
                        ((Double) Objects.requireNonNull(r.getValueByKey("_value"))).longValue() : 0L;

                results.merge(
                        formattedTime,
                        new DailyElectricity(
                                new DailyElectricity.Pk(channelId, formattedTime),
                                channelRepository.findById(channelId).orElse(null),
                                value,
                                0L
                        ),
                        (existing, newEntry) -> new DailyElectricity(
                                existing.getPk(),
                                existing.getChannel(),
                                existing.getKwh() + newEntry.getKwh(),
                                existing.getBill() + newEntry.getBill()
                        )
                );
            }
        }
        return new ArrayList<>(results.values());
    }

    private String getQuery(String place, String type, int organizationId, LocalDateTime start, LocalDateTime end) {
        String startRFC3339 = DateTimeFormatter.ISO_INSTANT.format(start.atZone(ZoneId.systemDefault()).toInstant());
        String endRFC3339 = DateTimeFormatter.ISO_INSTANT.format(end.atZone(ZoneId.systemDefault()).toInstant());
        StringBuilder fluxQuery = new StringBuilder("from(bucket: \"");
        fluxQuery.append(bucket).append("\")\n")
                .append("  |> range(start: ").append(startRFC3339).append(", stop: ").append(endRFC3339).append(")\n")
                .append("  |> filter(fn: (r) => ");
        List<Place> places = placeRepository.findAllByOrganization_Id(organizationId);

        if (place.equals("total") && places.size() > 1) {
            fluxQuery.append(
                    places.stream()
                            .map(p -> "r[\"place\"] == \"" + p.getPlaceName() + "\"")
                            .collect(Collectors.joining(" or "))
            );
        } else {
            fluxQuery.append("r[\"place\"] == \"")
                    .append(places.stream()
                            .filter(p -> place.equals(p.getPlaceName()))
                            .findFirst()
                            .orElseThrow(EntityNotFoundException::new)
                            .getPlaceName()
                    )
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

    private String getKwhQuery(String place, String type, LocalDateTime start, LocalDateTime end) {
        String startRFC3339 = DateTimeFormatter.ISO_INSTANT.format(start.atZone(ZoneId.systemDefault()).toInstant());
        String endRFC3339 = DateTimeFormatter.ISO_INSTANT.format(end.atZone(ZoneId.systemDefault()).toInstant());

        StringBuilder fluxQuery = new StringBuilder();
        fluxQuery.append("from(bucket: \"").append(bucket).append("\")\n")
                .append("  |> range(start: ").append(startRFC3339).append(", stop: ").append(endRFC3339).append(")\n");


        if (!Objects.equals(place, "total")) {
            fluxQuery.append("  |> filter(fn: (r) => r[\"place\"] == \"").append(place).append("\")\n");
        }

        fluxQuery.append("  |> filter(fn: (r) => r[\"type\"] == \"").append(type).append("\")\n")
                .append("  |> filter(fn: (r) => r[\"phase\"] == \"kwh\")\n")
                .append("  |> filter(fn: (r) => r[\"description\"] == \"sum\")\n")
                .append("  |> aggregateWindow(every: 1h, fn: last, createEmpty: false)\n");

        if (place.equals("total")) {
            fluxQuery.append("  |> group(columns: [\"_time\"])\n")
                    .append("  |> sum()\n")
                    .append("  |> group(mode: \"by\")");
        }

        return fluxQuery.toString();
    }

}
