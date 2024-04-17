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
    @Column(name = "sensor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "sensor_name")
    private String name;
    @Column(name = "sensor_model_name")
    private String modelName;
    @Column
    private int channel;
    @Column
    private String ip;
    @Column
    private String port;
    @Column
    @Enumerated(EnumType.STRING)
    private Alive alive;
    @JoinColumn(name = "organization_id")
    @ManyToOne
    private Organization organization;
}
