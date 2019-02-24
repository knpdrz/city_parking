package city.parking.controllers;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.entities.ParkingProcessPartialUpdateRequest;
import city.parking.exceptions.ParkingProcessNotFoundException;
import city.parking.services.ParkingProcessService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping(path = "/parking-processes")
public class ParkingProcessController {
    private final ParkingProcessService parkingProcessService;

    public ParkingProcessController(ParkingProcessService parkingProcessService){
        this.parkingProcessService = parkingProcessService;
    }

    @GetMapping
    public ResponseEntity<List<ParkingProcess>> getParkingProcessesByStage(
            @RequestParam(name = "stage", required = false) ParkingProcess.Stage processStage){
        List<ParkingProcess> processes;
        if(processStage != null)
            processes = parkingProcessService.findParkingProcessesByStage(processStage);
        else{
            processes = parkingProcessService.findAll();
        }
        return ResponseEntity.ok(processes);
    }

    @GetMapping(value = "/{processId}")
    public ResponseEntity<ParkingProcess> getParkingProcess(@PathVariable Integer processId){
        Optional<ParkingProcess> processOptional = parkingProcessService.findById(processId);
        if(processOptional.isPresent()){
            return ResponseEntity.ok(processOptional.get());
        }else{
            throw new ParkingProcessNotFoundException(processId);
        }
    }

    @PostMapping
    public ResponseEntity<ParkingProcess> startParkingProcess(UriComponentsBuilder ucBuilder,
                                                              @Valid @RequestBody ParkingProcess process) throws URISyntaxException {
        ParkingProcess newParkingProcess = parkingProcessService.startParkingProcess(process);
        URI newProcessLocationURI = ucBuilder.path("/parking-processes/{processId}")
                .buildAndExpand(newParkingProcess.getId()).toUri();

        return ResponseEntity.created(newProcessLocationURI)
                .body(newParkingProcess);
    }

    @PatchMapping(value = "/{processId}")
    public ResponseEntity stopParkingMeter(@PathVariable Integer processId,
                                           @RequestBody ParkingProcessPartialUpdateRequest parkingProcessPartialUpdateRequest){
        return new ResponseEntity<>(parkingProcessService
                .updateParkingProcessStage(processId, parkingProcessPartialUpdateRequest),
                HttpStatus.SEE_OTHER);
    }

    @GetMapping(value = "/{processId}/costs")
    public ResponseEntity<Set<Money>> getParkingCost(@PathVariable Integer processId){
        return ResponseEntity.ok(parkingProcessService.getParkingCosts(processId));
    }
}
