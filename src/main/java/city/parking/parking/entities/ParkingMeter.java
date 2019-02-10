package city.parking.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.joda.time.DateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Entity
@NoArgsConstructor
public class ParkingMeter {
    private @Id @GeneratedValue Integer id;
    private Integer spotId;
    private DateTime startTime;
    private DateTime stopTime;

    public ParkingMeter(Integer spotId){
        this.spotId = spotId;
    }

}
