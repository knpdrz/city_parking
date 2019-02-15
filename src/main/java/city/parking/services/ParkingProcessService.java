package city.parking.services;

import city.parking.repositories.ParkingProcessRepository;
import city.parking.entities.ParkingProcess;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ParkingProcessService {
    private static final double regularCostMultiplier = 1.5;
    private static final double costForDisabledMultiplier = 1.2;

    private final ParkingProcessRepository repository;

    public ParkingProcessService(ParkingProcessRepository repository){
        this.repository = repository;
    }

    public List<ParkingProcess> findAll() {
        return repository.findAll();
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
        parkingProcess.setCost(calculateParkingCost(parkingProcess));

        repository.save(parkingProcess);
        return parkingProcess;
    }

    public double getParkingCost(Integer meterId) {
        ParkingProcess parkingProcess = repository.findByMeterIdAndStage(meterId, ParkingProcess.Stage.STOPPED_UNPAID);
        return parkingProcess.getCost();
    }

    private int getParkingTimeInHours(ParkingProcess process){
        LocalDateTime startTime = process.getParkingStartTime();
        LocalDateTime stopTime = process.getParkingStopTime();
        long parkingTimeInMillis = Duration.between(startTime, stopTime).toMillis();
        long millisInHour = Duration.ofHours(1).toMillis();

        int parkingTimeInHours = (int)Math.ceil((double)parkingTimeInMillis/millisInHour);
        return parkingTimeInHours;
    }

    private double calculateParkingCost(ParkingProcess process){
        int parkingTimeInHours = getParkingTimeInHours(process);
        return process.isForDisabled() ? getDisabledCost(parkingTimeInHours) : getRegularCost(parkingTimeInHours);
    }

    private double getRegularCost(int hoursPassed){
        if(hoursPassed < 1) return 0;
        else if(hoursPassed == 1) return 1;
        else return 1+2*(1-Math.pow(regularCostMultiplier,hoursPassed-1))/(1-regularCostMultiplier);
    }

    private double getDisabledCost(int hoursPassed){
        if(hoursPassed <= 1) return 0;
        else return 2*(1-Math.pow(costForDisabledMultiplier,hoursPassed-1))/(1-costForDisabledMultiplier);
    }
}
