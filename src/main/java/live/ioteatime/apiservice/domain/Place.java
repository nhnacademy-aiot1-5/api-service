package live.ioteatime.apiservice.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "places")
@AllArgsConstructor
@NoArgsConstructor
public class Place {
    @Id
    private int placeId;
    @Column(name = "place_name")
    private String placeName;

    @ManyToOne
    @JoinColumn(name = "organization_id")
    private Organization organization;
}
