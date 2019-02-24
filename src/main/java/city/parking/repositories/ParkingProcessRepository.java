package city.parking.repositories;

import city.parking.entities.ParkingProcess;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingProcessRepository extends JpaRepository<ParkingProcess, Integer> {
    List<ParkingProcess> findByStage(ParkingProcess.Stage stage);
    List<ParkingProcess> findByMeterIdAndStage(Integer meterId, ParkingProcess.Stage stage);
}
