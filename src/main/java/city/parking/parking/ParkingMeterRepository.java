package city.parking.parking;

import city.parking.parking.entities.ParkingMeter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingMeterRepository extends JpaRepository<ParkingMeter, Integer> {
    ParkingMeter findBySpotId(Integer spotId);
}
