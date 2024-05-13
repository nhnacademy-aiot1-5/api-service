package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "supported_sensors")
@Getter @Setter @NoArgsConstructor
public class SupportedSensor {
    @Id
    @Column(name = "sensor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sensor_model_name")
    private String modelName;

    @Column(name = "protocol")
    @Enumerated(EnumType.STRING)
    private Protocol protocol;
}
