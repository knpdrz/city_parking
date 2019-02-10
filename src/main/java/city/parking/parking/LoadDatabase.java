package city.parking.parking;

import city.parking.parking.entities.ParkingMeter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(ParkingMeterRepository repository) {
        return args -> {
            log.info("Preloading " + repository.save(new ParkingMeter(5)));
            log.info("Preloading " + repository.save(new ParkingMeter(3)));
        };

    }
}
