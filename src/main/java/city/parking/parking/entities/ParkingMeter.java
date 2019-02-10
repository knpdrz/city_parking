package city.parking.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Slf4j
public class ParkingMeter {
    public enum State{
        UNUSED, RUNNING;
    }

    private @Id @GeneratedValue Integer id;
    private Integer spotId;
    private LocalDateTime startTime;
    private LocalDateTime stopTime;
    private State state;

    public ParkingMeter(Integer spotId){
        this.spotId = spotId;
        state = State.UNUSED;
    }

    public void startMeter(){
        if(state == State.UNUSED) {
            log.info("starting meter, state = " + state);
            state = State.RUNNING;
            startTime = LocalDateTime.now();
        }
    }

    public void stopMeter() {
        if(state == State.RUNNING) {
            log.info("stopping meter, state = " + state);
            state = State.UNUSED;
            stopTime = LocalDateTime.now();
        }
    }


    public double getCost(boolean isDisabled){
        long hoursPassed = Duration.between(startTime, stopTime).toHours();
        long minutesPassed = Duration.between(startTime, stopTime).toMinutes();

        if(minutesPassed > 0) hoursPassed ++;

        return isDisabled ? getDisabledCost(hoursPassed) : getRegularCost(hoursPassed);
    }

    private double getRegularCost(long hoursPassed){
        double multiplier = 1.5;
        if(hoursPassed < 1) return 0;
        else if(hoursPassed == 1) return 1;
        else return 1+2*(1-Math.pow(multiplier,hoursPassed-1))/(1-multiplier);
    }

    private double getDisabledCost(long hoursPassed){
        double multiplier = 1.2;
        if(hoursPassed <= 1) return 0;
        else return 2*(1-Math.pow(multiplier,hoursPassed-1))/(1-multiplier);
    }

}
