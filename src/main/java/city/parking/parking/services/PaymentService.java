package city.parking.parking.services;

import city.parking.parking.entities.ParkingProcess;
import city.parking.parking.repositories.ParkingProcessRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class PaymentService {
    private final ParkingProcessRepository repository;

    public PaymentService(ParkingProcessRepository repository){
        this.repository = repository;
    }

    public double getParkingCost(Integer meterId) {
        ParkingProcess process = repository.findByMeterIdAndStage(meterId, ParkingProcess.Stage.STOPPED_UNPAID);
        return calculateParkingCost(process);
    }

    private int getParkingTimeInHours(ParkingProcess process){
        long parkingTimeInMillis = Duration.between(process.getParkingStartTime(), process.getParkingStopTime()).toMillis();
        long millisInHour = Duration.ofHours(1).toMillis();
        int parkingTimeInHours = (int)Math.ceil((double)parkingTimeInMillis/millisInHour);
        return parkingTimeInHours;
    }

    private double calculateParkingCost(ParkingProcess process){
        int parkingTimeInHours = getParkingTimeInHours(process);
        double cost = process.isForDisabled() ? getDisabledCost(parkingTimeInHours) : getRegularCost(parkingTimeInHours);
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
