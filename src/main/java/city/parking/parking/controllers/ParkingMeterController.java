package city.parking.parking.controllers;

import city.parking.parking.ParkingMeterRepository;
import city.parking.parking.entities.ParkingMeter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
