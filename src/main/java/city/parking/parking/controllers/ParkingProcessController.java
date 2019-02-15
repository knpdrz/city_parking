package city.parking.parking.controllers;

import city.parking.parking.services.ParkingProcessService;
import city.parking.parking.entities.ParkingProcess;
import city.parking.parking.services.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/parking-processes")
public class ParkingProcessController {
    private final ParkingProcessService parkingProcessService;
    private final PaymentService paymentService;

    public ParkingProcessController(ParkingProcessService parkingProcessService,
                                    PaymentService paymentService){
        this.parkingProcessService = parkingProcessService;
        this.paymentService = paymentService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public ParkingProcess startParkingProcess(@RequestBody ParkingProcess process){
        return parkingProcessService.startParkingProcess(process);
    }

    @RequestMapping(value = "stop/{meterId}", method = RequestMethod.GET)
    public ParkingProcess stopParkingMeter(@PathVariable Integer meterId){
        return parkingProcessService.stopParkingMeter(meterId);
    }

    @RequestMapping(method = RequestMethod.GET)
    List<ParkingProcess> getOngoingParkingProcesses(){
        return parkingProcessService.findMetersByState(ParkingProcess.Stage.ONGOING);
    }

    @RequestMapping(value = "{meterId}/cost", method = RequestMethod.GET)
    double getParkingCost(@PathVariable Integer meterId){
        return paymentService.getParkingCost(meterId);
    }


}
