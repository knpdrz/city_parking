package city.parking.services;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.entities.ParkingProcessPartialUpdateRequest;
import city.parking.exceptions.ParkingMeterAlreadyInUseException;
import city.parking.exceptions.ParkingMeterNotRunningException;
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
        Integer toBeStartedMeterId = parkingProcess.getMeterId();
        ensureParkingMeterIsNotUsed(toBeStartedMeterId);

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
                ensureParkingMeterIsRunning(process);
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

    private void ensureParkingMeterIsNotUsed(Integer toBeStartedMeterId){
        //if parking meter is running or it is unpaid (parking process ONGOING or STOPPED_UNPAID stage)
        //it cannot be started
        List<ParkingProcess> ongoingParkingProcessesWithGivenMeterId =
                parkingProcessRepository.findByMeterIdAndStage(toBeStartedMeterId, ParkingProcess.Stage.ONGOING);
        List<ParkingProcess> unpaidParkingProcessesWithGivenMeterId =
                parkingProcessRepository.findByMeterIdAndStage(toBeStartedMeterId, ParkingProcess.Stage.STOPPED_UNPAID);

        if(ongoingParkingProcessesWithGivenMeterId.size() > 0 ||
                unpaidParkingProcessesWithGivenMeterId.size() > 0){
            throw new ParkingMeterAlreadyInUseException(toBeStartedMeterId);
        }
    }

    private void ensureParkingMeterIsRunning(ParkingProcess process) {
        //if parking meter is not running it cannot be stopped
        if(!process.getStage().equals(ParkingProcess.Stage.ONGOING)){
            throw new ParkingMeterNotRunningException(process.getId());
        }
    }

    private Set<Money> getDisplayableCosts(Money primaryCurrencyCost) {
        Set<Money> costs = new HashSet<>();
        costs.add(primaryCurrencyCost);
        return costs;
    }

    private void stopParkingMeter(ParkingProcess process) {
        process.setParkingStopTime(LocalDateTime.now());
        Money primaryParkingCost = calculatePrimaryParkingCost(process);
        process.setPrimaryCurrencyCost(primaryParkingCost);

        //do not wait for payment if parking was free
        if(primaryParkingCost.getAmount().compareTo(BigDecimal.ZERO) == 0) {
            process.setStage(ParkingProcess.Stage.PAID);
        }else {
            process.setStage(ParkingProcess.Stage.STOPPED_UNPAID);
        }
        parkingProcessRepository.save(process);
    }
}
