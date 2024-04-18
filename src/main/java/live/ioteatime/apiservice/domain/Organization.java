package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "organizations")

public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private int id;
    @Column
    private String name;
    @Column(name = "electricity_budget")
    private Long electricityBudget;
    @Column(name = "organization_code")
    private String organizationCode;
}
