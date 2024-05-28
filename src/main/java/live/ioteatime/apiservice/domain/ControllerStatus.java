package live.ioteatime.apiservice.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "controller_status")
@Getter
@Setter
@NoArgsConstructor
public class ControllerStatus {
    @Id
    @Column(name= "controller_id")
    private String id;
    @Column
    private int status;
}
