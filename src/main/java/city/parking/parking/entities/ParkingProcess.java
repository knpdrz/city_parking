package city.parking.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Setter
@Slf4j
public class ParkingProcess {
    public enum Stage {
        ONGOING, STOPPED_UNPAID;
    }

    private @Id @GeneratedValue Integer id;
    private Integer meterId;
    private LocalDateTime parkingStartTime;
    private LocalDateTime parkingStopTime;
    private Stage stage;
    private boolean isForDisabled;

}
