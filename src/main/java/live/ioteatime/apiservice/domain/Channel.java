package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "channels")
@Getter
@Setter
@NoArgsConstructor
public class Channel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "channel_id")
    private int id;

    @JoinColumn(name = "sensor_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private ModbusSensor sensor;

    @JoinColumn(name = "place_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;

    @Column(name = "channel_name")
    private String channelName;
}
