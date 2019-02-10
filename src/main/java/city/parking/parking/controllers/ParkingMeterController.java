package city.parking.parking.controllers;

import city.parking.parking.ParkingMeterNotFoundException;
import city.parking.parking.ParkingMeterRepository;
import city.parking.parking.entities.ParkingMeter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.procedure.ParameterMisuseException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
public class ParkingMeterController {
    private final ParkingMeterRepository repository;

    public ParkingMeterController(ParkingMeterRepository repository){
        this.repository = repository;
    }

    @GetMapping("/meters")
    List<ParkingMeter> all(){
        return repository.findAll();
    }

    @PutMapping("/meters/{spotId}/start")
    void startMeter(@PathVariable Integer spotId){
        List<ParkingMeter> meters = repository.findBySpotId(spotId);
        if(meters.isEmpty()) throw new ParkingMeterNotFoundException(spotId);

        ParkingMeter meter = meters.get(0);
        meter.startMeter();
        repository.save(meter);
    }

    @PutMapping("/meters/{spotId}/stop")
    void stopMeter(@PathVariable Integer spotId){
        List<ParkingMeter> meters = repository.findBySpotId(spotId);
        if(meters.isEmpty()) throw new ParkingMeterNotFoundException(spotId);

        ParkingMeter meter = meters.get(0);

        meter.stopMeter();
        repository.save(meter);
    }

    @GetMapping("/meters/{spotId}/cost")
    double getParkingCost(@RequestParam boolean disabled, @PathVariable Integer spotId){
        List<ParkingMeter> meters = repository.findBySpotId(spotId);
        if(meters.isEmpty()) throw new ParkingMeterNotFoundException(spotId);

        ParkingMeter meter = meters.get(0);

        return meter.getCost(disabled);
    }
}
