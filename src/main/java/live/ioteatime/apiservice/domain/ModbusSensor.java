package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "modbus_sensors")
@NoArgsConstructor
@Getter @Setter
public class ModbusSensor {

    @Id
    @Column(name = "sensor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sensor_name")
    private String sensorName;

    @Column(name = "sensor_model_name")
    private String modelName;

    @Column
    private String ip;

    @Column
    private String port;

    @Column(name = "channel_count")
    private int channelCount;

    @Column
    @Enumerated(EnumType.STRING)
    private Alive alive;

    @JoinColumn(name = "organization_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

}