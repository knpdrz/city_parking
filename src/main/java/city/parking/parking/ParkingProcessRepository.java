package city.parking.parking;

import city.parking.parking.entities.ParkingProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingProcessRepository extends JpaRepository<ParkingProcess, Integer> {
    List<ParkingProcess> findByStage(ParkingProcess.Stage stage);
    ParkingProcess findByMeterIdAndStage(Integer meterId, ParkingProcess.Stage stage);
}
