package live.ioteatime.apiservice.domain;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "topics")
@Getter @Setter @NoArgsConstructor
public class Topic {
    @EmbeddedId
    private Pk pk;

    @MapsId("sensorId")
    @JoinColumn
    @ManyToOne
    private Sensor sensor;

    @Column
    private String topic;

    @Embeddable
    @NoArgsConstructor @AllArgsConstructor
    @EqualsAndHashCode
    @Getter @Setter
    public static class Pk implements Serializable {

        @Column
        private int channel;

        @Column(name = "sensor_id")
        private int sensorId;
    }
}
