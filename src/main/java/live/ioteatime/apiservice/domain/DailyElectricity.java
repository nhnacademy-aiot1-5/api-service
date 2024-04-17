package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "daily_electricity_consumption")
public class DailyElectricity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long kwh;
    @Column(unique = true)
    private LocalDate time;
}
