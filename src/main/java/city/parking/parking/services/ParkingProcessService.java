package city.parking.parking.services;

import city.parking.parking.repositories.ParkingProcessRepository;
import city.parking.parking.entities.ParkingProcess;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingProcessService {
    private final ParkingProcessRepository repository;

    public ParkingProcessService(ParkingProcessRepository repository){
        this.repository = repository;
    }

    public List<ParkingProcess> findMetersByState(ParkingProcess.Stage processStage) {
        return repository.findByStage(processStage);
    }

    public ParkingProcess startParkingProcess(ParkingProcess parkingProcess) {
        parkingProcess.setParkingStartTime(LocalDateTime.now());
        parkingProcess.setStage(ParkingProcess.Stage.ONGOING);
        repository.save(parkingProcess);
        return parkingProcess;
    }

    public ParkingProcess stopParkingMeter(Integer meterId) {
        ParkingProcess parkingProcess = repository.findByMeterIdAndStage(meterId, ParkingProcess.Stage.ONGOING);
        parkingProcess.setParkingStopTime(LocalDateTime.now());
        parkingProcess.setStage(ParkingProcess.Stage.STOPPED_UNPAID);
        repository.save(parkingProcess);
        return parkingProcess;
    }



}
