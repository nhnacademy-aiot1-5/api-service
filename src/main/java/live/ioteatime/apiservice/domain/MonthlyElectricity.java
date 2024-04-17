package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "monthly_electricity_consumption")
@Getter
@Setter
public class MonthlyElectricity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long kwh;
    @Column(unique = true)
    private LocalDate time;
}
