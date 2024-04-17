package live.ioteatime.apiservice.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "organization")
public class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "organization_id")
    private int id;
    private String name;
    @Column(name = "electricity_budget")
    private Long electricityBudget;
    @Column(name = "organization_code")
    private String organizationCode;
}
