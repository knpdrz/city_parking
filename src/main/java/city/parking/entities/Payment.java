package city.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@Setter
public class Payment {
    private @Id @GeneratedValue Integer id;
    private Integer parkingProcessId;
    private Integer amount;
}
