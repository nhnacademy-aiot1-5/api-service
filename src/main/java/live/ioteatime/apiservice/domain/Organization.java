package live.ioteatime.apiservice.domain;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    @Column(name = "electricity_budget")
    private BigInteger electricityBudget;
}
