package city.parking.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private Double currentDailyIncome;

    public ParkingMeter(Integer spotId){
        this.spotId = spotId;
        state = State.UNUSED;
        currentDailyIncome = 0.0;
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

    public void pay(boolean isDisabled){
        double cost = getCost(isDisabled);
        currentDailyIncome += cost;
        log.info("current daily income = " + currentDailyIncome);
    }

    public double getCost(boolean isDisabled){
        long billingPeriodDurationMilis = Duration.between(startTime, stopTime).toMillis();
        int startedHours = (int)Math.ceil((double)billingPeriodDurationMilis/Duration.ofHours(1).toMillis());
        double cost = isDisabled ? getDisabledCost(startedHours) : getRegularCost(startedHours);
        return cost;
    }

    private double getRegularCost(int hoursPassed){
        double multiplier = 1.5;
        if(hoursPassed < 1) return 0;
        else if(hoursPassed == 1) return 1;
        else return 1+2*(1-Math.pow(multiplier,hoursPassed-1))/(1-multiplier);
    }

    private double getDisabledCost(int hoursPassed){
        double multiplier = 1.2;
        if(hoursPassed <= 1) return 0;
        else return 2*(1-Math.pow(multiplier,hoursPassed-1))/(1-multiplier);
    }

}
