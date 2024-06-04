package live.ioteatime.apiservice.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_electricity_consumption")
@Getter
@Setter
public class MonthlyElectricity {

    @EmbeddedId
    private Pk pk;

    @MapsId("channelId")
    @JoinColumn(name = "channel_id")
    @ManyToOne
    private Channel channel;

    private Double kwh;

    private Long bill;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    @EqualsAndHashCode
    public static class Pk implements Serializable {
        @Column(name = "channel_id")
        private int channelId;
        @Column(name = "time")
        private LocalDateTime time;
    }

}


