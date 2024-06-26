package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "budget_history")
public class BudgetHistory {

    @Id
    @Column(name = "history_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "change_time")
    private LocalDateTime changeTime;

    @Column
    private Long budget;

    @JoinColumn(name = "organization_id")
    @ManyToOne(fetch = FetchType.LAZY)
    Organization organization;

}
