package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@NoArgsConstructor
@Getter
@Setter
public class User {
    @Id
    @Column
    private String id;
    @Column
    private String password;
    @Column
    private String name;
    @Column(name = "created_at")
    private LocalDate createdAt;
    @Column
    @Enumerated(EnumType.STRING)
    private Role role;
    @JoinColumn(name = "organization_id")
    @ManyToOne
    private Organization organization;
}
