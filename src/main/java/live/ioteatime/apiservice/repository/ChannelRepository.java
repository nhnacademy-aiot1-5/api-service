package live.ioteatime.apiservice.repository;

import live.ioteatime.apiservice.domain.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Integer> {
    List<Channel> findAllBySensor_Id(Integer sensorId);

    List<Channel> findAllByPlace_Id(int placeId);

    List<Channel> findAllByChannelName(String channelName);
}
