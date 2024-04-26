package live.ioteatime.apiservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "hourly_electricity_consumption")
public class HourlyElectricity {
    @EmbeddedId
    private Pk pk;

    @MapsId("channelId")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "channel_id")
    private Channel channel;

    private long kwh;
    private long bill;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Setter
    @Getter
    public static class Pk implements Serializable {
        private LocalDateTime time;

        @Column(name = "channel_id")
        private String channelId;
    }
}
