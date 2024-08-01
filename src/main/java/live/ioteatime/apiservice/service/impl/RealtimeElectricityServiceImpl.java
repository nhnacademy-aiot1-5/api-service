package live.ioteatime.apiservice.service.impl;

import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.domain.Place;
import live.ioteatime.apiservice.dto.electricity.RealtimeElectricityResponseDto;
import live.ioteatime.apiservice.repository.ChannelRepository;
import live.ioteatime.apiservice.repository.PlaceRepository;
import live.ioteatime.apiservice.service.RealtimeElectricityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class RealtimeElectricityServiceImpl implements RealtimeElectricityService {
    @Value("${spring.influx.bucket}")
    private String bucket;
    @Value("${spring.influx.org}")
    private String organization;

    private final ChannelRepository channelRepository;
    private final PlaceRepository placeRepository;
    private final InfluxDBClient influxDBClient;

    @Override
    public List<RealtimeElectricityResponseDto> getRealtimeElectricity(int organizationId){
        LocalDateTime time = LocalDateTime.now().minusHours(9).withNano(0);
        List<Place> placeList = placeRepository.findAllByOrganization_Id(organizationId);
        List<RealtimeElectricityResponseDto> result = new ArrayList<>();

        for (Place place: placeList){
            List<Channel> channelList = channelRepository.findAllByPlace_Id(place.getId());
            for (Channel channel : channelList) {
                String query = getQuery(place.getPlaceName(), channel.getChannelName(), time.minusHours(1), time);
                QueryApi queryApi = influxDBClient.getQueryApi();
                List<FluxTable> tables = queryApi.query(query, organization);
                if (tables.isEmpty() || channel.getChannelName().equals("main")){
                    log.debug("이거 실행되면 오케이");
                    continue;
                }
                FluxTable table = tables.get(0);
                FluxRecord r = table.getRecords().get(0);
                Double value = (Double) r.getValue();
                result.add(new RealtimeElectricityResponseDto(place.getPlaceName(), channel.getChannelName(), value));

            }
        }
        return result;
    }


    private String getQuery(String place, String type, LocalDateTime start, LocalDateTime end) {
        String startRFC3339 = DateTimeFormatter.ISO_INSTANT.format(start.atZone(ZoneId.systemDefault()).toInstant());
        String endRFC3339 = DateTimeFormatter.ISO_INSTANT.format(end.atZone(ZoneId.systemDefault()).toInstant());

        StringBuilder fluxQuery = new StringBuilder();
        fluxQuery.append("from(bucket: \"").append(bucket).append("\")\n")
                .append("  |> range(start: ").append(startRFC3339).append(", stop: ").append(endRFC3339).append(")\n")
                .append("  |> filter(fn: (r) => r[\"place\"] == \"").append(place).append("\")\n")
                .append("  |> filter(fn: (r) => r[\"type\"] == \"").append(type).append("\")\n")
                .append("  |> filter(fn: (r) => r[\"phase\"] == \"total\")\n")
                .append("  |> filter(fn: (r) => r[\"description\"] == \"w\")\n")
                .append("  |> aggregateWindow(every: 5s, fn: last, createEmpty: false)\n")
                .append("  |> tail(n: 1)")
                .append("  |> yield()");

        return fluxQuery.toString();
    }
}
