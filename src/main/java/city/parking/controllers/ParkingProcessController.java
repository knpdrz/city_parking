package city.parking.controllers;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.entities.ParkingProcessPartialUpdateRequest;
import city.parking.services.ParkingProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping(path = "/parking-processes")
public class ParkingProcessController {
    private final ParkingProcessService parkingProcessService;

    public ParkingProcessController(ParkingProcessService parkingProcessService){
        this.parkingProcessService = parkingProcessService;
    }

    @GetMapping
    public ResponseEntity<List<ParkingProcess>> getParkingProcessesByStage(@RequestParam(name = "stage", required = false) ParkingProcess.Stage processStage){
        if(processStage != null)
            return ResponseEntity.ok(parkingProcessService.findMetersByState(processStage));
        return ResponseEntity.ok(parkingProcessService.findAll());
    }

    @PostMapping
    public ResponseEntity<ParkingProcess> startParkingProcess(@RequestBody ParkingProcess process){
        return ResponseEntity.ok(parkingProcessService.startParkingProcess(process));
    }

    @PatchMapping(value = "/{processId}")
    public ResponseEntity stopParkingMeter(@PathVariable Integer processId,
                                           @Valid @RequestBody ParkingProcessPartialUpdateRequest parkingProcessPartialUpdateRequest){
        return new ResponseEntity<>(parkingProcessService
                .updateParkingProcessStage(processId, parkingProcessPartialUpdateRequest),
                HttpStatus.SEE_OTHER);

    }

    @GetMapping(value = "/{processId}/costs")
    public Set<Money> getParkingCost(@PathVariable Integer processId){
        return parkingProcessService.getParkingCosts(processId);
    }
}
