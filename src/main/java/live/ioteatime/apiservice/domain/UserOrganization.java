package live.ioteatime.apiservice.domain;

import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "user_organization")
public class UserOrganization {

    @EmbeddedId
    private Pk pk;

    @MapsId("organizationId")
    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;

    @MapsId("userId")
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Embeddable
    @NoArgsConstructor
    @EqualsAndHashCode
    public static class Pk implements Serializable {
        @Column(name = "organization_id")
        private int organizationId;

        @Column(name = "user_id")
        private String userId;
    }
}
