package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "outlier")
@Getter
@Setter
@NoArgsConstructor
public class Outlier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "outlier_id")
    int id;

    @Column
    String place;

    @Column
    String type;

    @Column
    long time;

    @Column(name = "outlier_value")
    double outlierValue;

    @Column
    int flag;

    @JoinColumn(name = "organization_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Organization organization;

}
