package live.ioteatime.apiservice.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "mqtt_sensors")
@NoArgsConstructor
@Getter
@Setter
public class MqttSensor {
    @Id
    @Column(name = "sensor_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "sensor_name")
    private String name;

    @Column(name = "sensor_model_name")
    private String modelName;

    @Column
    private String ip;

    @Column
    private String port;

    @Column
    @Enumerated(EnumType.STRING)
    private Alive alive;

    @JoinColumn(name = "organization_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

    @JoinColumn(name = "place_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Place place;
}
