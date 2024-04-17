package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "sensors")
@NoArgsConstructor
@Getter
@Setter
public class Sensor {
    @Id
    @Column(name = "sensor_number")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int sensorNumber;
    @Column(name = "sensor_name")
    private String sensorName;
    @Column
    @Enumerated(EnumType.STRING)
    private Alive alive;
}
