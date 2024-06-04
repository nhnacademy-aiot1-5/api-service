package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "daily_predicted_electricity_consumption")
@Getter
@Setter
public class DailyPredictedElectricity {

    @Id
    LocalDateTime time;

    Double kwh;

    Long bill;

    @Column(name = "organization_id")
    Integer organizationId;

    @Column(name = "channel_id")
    Integer channelId;

}
