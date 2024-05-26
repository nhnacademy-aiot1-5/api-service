package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "outlier")
@Getter
@Setter
@NoArgsConstructor
public class Outlier {
    @Id
    @Column(name = "outlier_id")
    int id;
    @Column
    String place;
    @Column
    String type;
    @Column(name = "outlier_value")
    double outlierValue;
    @Column
    int flag;
}
