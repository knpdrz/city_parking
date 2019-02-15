package city.parking.parking;

import city.parking.parking.entities.ParkingProcess;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(ParkingProcessRepository repository) {
        return args -> {
         //   log.info("Preloading " + repository.save(new ParkingProcess(5)));
           // log.info("Preloading " + repository.save(new ParkingProcess(3)));
        };

    }
}
