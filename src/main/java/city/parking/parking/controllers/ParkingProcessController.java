package city.parking.parking.controllers;

import city.parking.parking.ParkingProcessService;
import city.parking.parking.entities.ParkingProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/parking-processes")
public class ParkingProcessController {
    private final ParkingProcessService service;

    public ParkingProcessController(ParkingProcessService service){
        this.service = service;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ParkingProcess startParkingProcess(@RequestBody ParkingProcess process){
        return service.startParkingProcess(process);
    }

    @RequestMapping(value = "stop/{meterId}", method = RequestMethod.GET)
    public ParkingProcess stopParkingMeter(@PathVariable Integer meterId){
        return service.stopParkingMeter(meterId);
    }

    @RequestMapping(method = RequestMethod.GET)
    List<ParkingProcess> getOngoingParkingProcesses(){
        return service.findMetersByState(ParkingProcess.Stage.ONGOING);
    }


}
