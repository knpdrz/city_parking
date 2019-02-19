package city.parking.controllers;

import city.parking.ParkingProcessStageConverter;
import city.parking.entities.ParkingProcess;
import city.parking.services.ParkingProcessService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/parking-processes")
public class ParkingProcessController {
    private final ParkingProcessService parkingProcessService;

    public ParkingProcessController(ParkingProcessService parkingProcessService){
        this.parkingProcessService = parkingProcessService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public List<ParkingProcess> getParkingProcessesByStage(@RequestParam(name = "stage", required = false) ParkingProcess.Stage processStage){
        if(processStage != null)
            return parkingProcessService.findMetersByState(processStage);
        return parkingProcessService.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    public ParkingProcess startParkingProcess(@RequestBody ParkingProcess process){
        return parkingProcessService.startParkingProcess(process);
    }

    @RequestMapping(value = "stop/{meterId}", method = RequestMethod.GET)
    public ParkingProcess stopParkingMeter(@PathVariable Integer meterId){
        return parkingProcessService.stopParkingMeter(meterId);
    }

    @RequestMapping(value = "{meterId}/cost", method = RequestMethod.GET)
    public String getParkingCost(@PathVariable Integer meterId){
        return parkingProcessService.getParkingCost(meterId) + " PLN";
    }

}
