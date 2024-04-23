package live.ioteatime.apiservice.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "daily_electricity_consumption")
@AllArgsConstructor
@NoArgsConstructor
public class DailyElectricity {
    @EmbeddedId
    private Pk pk;

    @MapsId("organizationId")
    @JoinColumn(name = "organization_id")
    @ManyToOne
    private Organization organization;
    private Long kwh;

    @Embeddable
    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class Pk implements Serializable {
        @Column(name = "organization_id")
        private int organizationId;
        @Column(name = "time")
        private LocalDateTime time;
    }
}
