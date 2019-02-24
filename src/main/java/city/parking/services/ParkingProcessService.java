package city.parking.services;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.entities.ParkingProcessPartialUpdateRequest;
import city.parking.exceptions.ParkingProcessNotFoundException;
import city.parking.repositories.ParkingProcessRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static city.parking.services.ParkingCostCalculationUtil.calculatePrimaryParkingCost;

@Service
public class ParkingProcessService {

    private final ParkingProcessRepository parkingProcessRepository;

    public ParkingProcessService(ParkingProcessRepository parkingProcessRepository) {
        this.parkingProcessRepository = parkingProcessRepository;
    }

    public List<ParkingProcess> findAll() {
        return parkingProcessRepository.findAll();
    }

    public List<ParkingProcess> findParkingProcessesByStage(ParkingProcess.Stage processStage) {
        return parkingProcessRepository.findByStage(processStage);
    }

    public Optional<ParkingProcess> findById(Integer processId) {
        return parkingProcessRepository.findById(processId);
    }

    public ParkingProcess startParkingProcess(ParkingProcess parkingProcess) {
        parkingProcess.setParkingStartTime(LocalDateTime.now());
        parkingProcess.setStage(ParkingProcess.Stage.ONGOING);
        parkingProcess.setPrimaryCurrencyCost(new Money(ParkingCostCalculationUtil.getPrimaryCurrency(), new BigDecimal("0")));
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
        } else {
            throw new ParkingProcessNotFoundException(processId);
        }
    }

    public Set<Money> getParkingCosts(Integer processId) {
        Optional<ParkingProcess> parkingProcessOptional = parkingProcessRepository.findById(processId);
        if (parkingProcessOptional.isPresent()) {
            Money primaryCurrencyCost = parkingProcessOptional.get().getPrimaryCurrencyCost();
            return getDisplayableCosts(primaryCurrencyCost);
        } else {
            throw new ParkingProcessNotFoundException(processId);
        }
    }

    private Set<Money> getDisplayableCosts(Money primaryCurrencyCost) {
        Set<Money> costs = new HashSet<>();
        costs.add(primaryCurrencyCost);
        return costs;
    }

    private void stopParkingMeter(ParkingProcess process) {
        process.setParkingStopTime(LocalDateTime.now());
        process.setStage(ParkingProcess.Stage.STOPPED_UNPAID);
        process.setPrimaryCurrencyCost(calculatePrimaryParkingCost(process));
        parkingProcessRepository.save(process);
    }
}
