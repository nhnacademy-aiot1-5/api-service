package live.ioteatime.apiservice.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.MonthlyElectricity;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.electricity.ElectricityRequestDto;
import live.ioteatime.apiservice.dto.electricity.ElectricityResponseDto;
import live.ioteatime.apiservice.exception.ElectricityNotFoundException;
import live.ioteatime.apiservice.repository.ChannelRepository;
import live.ioteatime.apiservice.repository.MonthlyElectricityRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.ElectricityService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service("monthlyElectricityService")
@RequiredArgsConstructor
public class MonthlyElectricityServiceImpl implements ElectricityService {
    @Value("${spring.influx.bucket}")
    private String bucket;
    @Value("${spring.influx.org}")
    private String organization;
    private final InfluxDBClient influxDBClient;
    private final MonthlyElectricityRepository monthlyElectricityRepository;
    private final ChannelRepository channelRepository;
    private final PlaceRepository placeRepository;

    @Override
    public ElectricityResponseDto getElectricityByDate(ElectricityRequestDto electricityRequestDto) {
        MonthlyElectricity.Pk pk = new MonthlyElectricity.Pk(electricityRequestDto.getChannelId(), electricityRequestDto.getTime());
        MonthlyElectricity monthlyElectricity = monthlyElectricityRepository.findMonthlyElectricityByPk(pk)
                .orElseThrow(() -> new ElectricityNotFoundException("monthly Electricity not found."));
        return new ElectricityResponseDto(monthlyElectricity.getPk().getTime(), monthlyElectricity.getKwh(), monthlyElectricity.getBill());
    }

    @Override
    public List<ElectricityResponseDto> getElectricitiesByDate(ElectricityRequestDto electricityRequestDto) {
        LocalDateTime endOfMonth = electricityRequestDto.getTime().withDayOfMonth(1).minusDays(1);
        LocalDateTime startOfTwelveMonthsBefore = endOfMonth.minusMonths(11).withDayOfMonth(1);

        List<MonthlyElectricity> monthlyElectricities = monthlyElectricityRepository.findAllByPkChannelIdAndPkTimeBetween(
                electricityRequestDto.getChannelId(),
                startOfTwelveMonthsBefore,
                endOfMonth
        );
        List<ElectricityResponseDto> responseDtos = new ArrayList<>();

        for (MonthlyElectricity monthlyElectricity : monthlyElectricities) {
            responseDtos.add(new ElectricityResponseDto(monthlyElectricity.getPk().getTime(), monthlyElectricity.getKwh(), monthlyElectricity.getBill()));
        }
        return responseDtos;
    }

    @Override
    public ElectricityResponseDto getCurrentElectricity() {
        LocalDateTime start = LocalDateTime.now().minusMonths(1).plusDays(1);
        LocalDateTime end = LocalDateTime.now();

        String fluxQuery = getKwhQuery("total", "main", start, end);
        QueryApi queryApi = influxDBClient.getQueryApi();
        FluxTable fluxTable = queryApi.query(fluxQuery, organization).get(0);
        List<FluxRecord> records = fluxTable.getRecords();

        double result = 0;
        if (!records.isEmpty()) {
            FluxRecord firstRecord = records.get(0);
            FluxRecord lastRecord = records.get(records.size() - 1);

            Double firstKwh = (Double) firstRecord.getValueByKey("_value");
            Double lastKwh = (Double) lastRecord.getValueByKey("_value");

            result = lastKwh - firstKwh;
        }

        return new ElectricityResponseDto(end, result, 0L);
    }

    @Override
    public ElectricityResponseDto getLastElectricity() {
        List<Channel> channels = channelRepository.findAllByChannelName("main");
        LocalDateTime lastMonth = LocalDateTime.now().minusMonths(1).toLocalDate().atStartOfDay();

        double kwh = 0;
        for (Channel channel : channels) {
            int channelId = channel.getId();
            MonthlyElectricity.Pk pk = new MonthlyElectricity.Pk(channelId, lastMonth);
            Optional<MonthlyElectricity> monthlyElectricity = monthlyElectricityRepository.findMonthlyElectricityByPk(pk);
            if (monthlyElectricity.isPresent()) {
                kwh += monthlyElectricity.get().getKwh();
            }
        }
        return new ElectricityResponseDto(lastMonth, kwh, 0L);
    }

    @Override
    public List<ElectricityResponseDto> getTotalElectricitiesByDate(LocalDateTime localDateTime, int organizationId) {
        LocalDateTime start = localDateTime.withMonth(1).withDayOfMonth(1)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime end = localDateTime.minusMonths(1).withDayOfMonth(31)
                .withHour(0).withMinute(0).withSecond(0).withNano(0);

        Map<LocalDateTime, ElectricityResponseDto> totalKwh = new HashMap<>();
        for (LocalDateTime date = start; !date.isAfter(end); date = date.plusDays(1)) {
            totalKwh.put(date, new ElectricityResponseDto(date, 0.0, null));
        }

        List<Place> placeList = placeRepository.findAllByOrganization_Id(organizationId);
        for (Place place : placeList) {
            Channel channel = channelRepository.findByPlaceAndChannelName(place, "main");
            if (channel == null) {
                continue;
            }
            List<MonthlyElectricity> currentMonthDataList = monthlyElectricityRepository
                    .findAllByPkChannelIdAndPkTimeBetween(channel.getId(), start, end);
            for (MonthlyElectricity data : currentMonthDataList) {
                LocalDateTime key = data.getPk().getTime();
                double kwh = totalKwh.get(key).getKwh();
                totalKwh.get(key).setKwh(kwh + data.getKwh());
            }
        }
        return new ArrayList<>(totalKwh.values());
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
                .append("  |> aggregateWindow(every: 1d, fn: last, createEmpty: false)\n");

        if (place.equals("total")) {
            fluxQuery.append("  |> group(columns: [\"_time\"])\n")
                    .append("  |> sum()\n")
                    .append("  |> group(mode: \"by\")");
        }

        return fluxQuery.toString();
    }
}
