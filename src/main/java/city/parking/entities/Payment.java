package city.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Setter
public class Payment {
    @Id @GeneratedValue private Integer id;
    private Integer parkingProcessId;
    @Embedded private Money balancePaid;
    private LocalDateTime date;
}
