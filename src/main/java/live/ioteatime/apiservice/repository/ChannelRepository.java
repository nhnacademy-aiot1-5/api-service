package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Channel;
import live.ioteatime.apiservice.dto.ChannelDto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    List<Channel> findALLBySensor_Id(Integer sensorId);
}
