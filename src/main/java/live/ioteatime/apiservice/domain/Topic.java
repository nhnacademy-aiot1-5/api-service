package live.ioteatime.apiservice.domain;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "topics")
@Getter @Setter @NoArgsConstructor
public class Topic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "topic_id")
    private int id;

    @Column
    private String topic;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name = "sensor_id")
    private MqttSensor mqttSensor;

}
