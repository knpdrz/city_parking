package city.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@Setter
public class ParkingProcess {
    public enum Stage {
        ONGOING, STOPPED_UNPAID, PAID;
    }

    @Id @GeneratedValue private Integer id;
    private Integer meterId;
    private LocalDateTime parkingStartTime;
    private LocalDateTime parkingStopTime;
    private Stage stage;
    private boolean forDisabled;
    @Embedded private Money primaryCurrencyCost;

}
