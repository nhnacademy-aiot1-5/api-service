package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "predicted_daily_electricity_consumption")
@Getter @Setter
public class PredictedDailyElectricity {
    @Id
    LocalDateTime time;
    Double kwh;
}
