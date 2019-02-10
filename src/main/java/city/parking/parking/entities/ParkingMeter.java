package city.parking.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
@Slf4j
public class ParkingMeter {
    enum State{
        UNUSED, RUNNING;
    }

    private @Id @GeneratedValue Integer id;
    private Integer spotId;
    private DateTime startTime;
    private DateTime stopTime;
    private State state;

    public ParkingMeter(Integer spotId){
        this.spotId = spotId;
        state = State.UNUSED;
    }

    public void startMeter(){
        state = State.RUNNING;
        log.info("starting meter, state = " + state);
    }

    public void stopMeter() {
        state = State.UNUSED;
        log.info("stopping meter, state = " + state);

    }

}
