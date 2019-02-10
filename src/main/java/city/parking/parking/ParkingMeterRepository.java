package city.parking.parking;

import city.parking.parking.entities.ParkingMeter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParkingMeterRepository extends JpaRepository<ParkingMeter, Integer> {

}
