package city.parking.services;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.entities.ParkingProcessPartialUpdateRequest;
import city.parking.exceptions.ParkingProcessNotFoundException;
import city.parking.repositories.ParkingProcessRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ParkingProcessService {
    private static final double regularCostMultiplier = 1.5;
    private static final double costForDisabledMultiplier = 1.2;
    private static final Currency primaryCurrency = Currency.getInstance("PLN");
    private static final int roundingScale = 2; //number of decimal places of money amount to keep

    private final ParkingProcessRepository parkingProcessRepository;

    public ParkingProcessService(ParkingProcessRepository parkingProcessRepository){
        this.parkingProcessRepository = parkingProcessRepository;
    }

    public List<ParkingProcess> findAll() {
        return parkingProcessRepository.findAll();
    }

    public List<ParkingProcess> findParkingProcessesByStage(ParkingProcess.Stage processStage) {
        return parkingProcessRepository.findByStage(processStage);
    }

    public Optional<ParkingProcess> findById(Integer processId){
        return parkingProcessRepository.findById(processId);
    }

    public ParkingProcess startParkingProcess(ParkingProcess parkingProcess) {
        parkingProcess.setParkingStartTime(LocalDateTime.now());
        parkingProcess.setStage(ParkingProcess.Stage.ONGOING);
        parkingProcess.setPrimaryCurrencyCost(new Money(primaryCurrency,new BigDecimal(0)));
        parkingProcessRepository.save(parkingProcess);
        return parkingProcess;
    }

    public ParkingProcess updateParkingProcessStage(Integer processId,
                                          ParkingProcessPartialUpdateRequest parkingProcessPartialUpdateRequest) {
        Optional<ParkingProcess> processOptional = parkingProcessRepository.findById(processId);
        if (processOptional.isPresent()) {
            ParkingProcess process = processOptional.get();
            if (parkingProcessPartialUpdateRequest.isParkingMeterToBeStopped()) {
                stopParkingMeter(process);
            }
            return process;
        }else{
            throw new ParkingProcessNotFoundException(processId);
        }
    }

    public Set<Money> getParkingCosts(Integer processId) {
        Optional<ParkingProcess> parkingProcessOptional = parkingProcessRepository.findById(processId);
        if(parkingProcessOptional.isPresent()){
            Money primaryCurrencyCost = parkingProcessOptional.get().getPrimaryCurrencyCost();
            return getDisplayableCosts(primaryCurrencyCost);
        }else{
            throw new ParkingProcessNotFoundException(processId);
        }
    }
    
    private Set<Money> getDisplayableCosts(Money primaryCurrencyCost){
        Set<Money> costs = new HashSet<>();
        costs.add(primaryCurrencyCost);
        return costs;
    }

    private int getParkingTimeInHours(ParkingProcess process){
        LocalDateTime startTime = process.getParkingStartTime();
        LocalDateTime stopTime = process.getParkingStopTime();
        long parkingTimeInMillis = Duration.between(startTime, stopTime).toMillis();
        long millisInHour = Duration.ofHours(1).toMillis();

        int parkingTimeInHours = (int)Math.ceil((double)parkingTimeInMillis/millisInHour);
        return parkingTimeInHours;
    }

    private Money calculatePrimaryParkingCost(ParkingProcess process){
        int parkingTimeInHours = getParkingTimeInHours(process);
        double primaryCost = process.isForDisabled() ? getDisabledCost(parkingTimeInHours) : getRegularCost(parkingTimeInHours);
        BigDecimal scaledPrimaryCost = new BigDecimal(primaryCost);
        scaledPrimaryCost = scaledPrimaryCost.setScale(roundingScale, RoundingMode.HALF_EVEN);
        return new Money(primaryCurrency, scaledPrimaryCost);
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

    private void stopParkingMeter(ParkingProcess process){
        process.setParkingStopTime(LocalDateTime.now());
        process.setStage(ParkingProcess.Stage.STOPPED_UNPAID);
        process.setPrimaryCurrencyCost(calculatePrimaryParkingCost(process));
        parkingProcessRepository.save(process);
    }
}
