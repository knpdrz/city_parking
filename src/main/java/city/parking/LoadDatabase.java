package city.parking;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.repositories.ParkingProcessRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

@Configuration
@Slf4j
public class LoadDatabase {
    @Bean
    CommandLineRunner initDatabase(ParkingProcessRepository repository) {
        return args -> {
            ParkingProcess p1 = new ParkingProcess();
            p1.setMeterId(100);
            p1.setParkingStartTime(LocalDateTime.of(2019, 2, 1, 7, 15));
            p1.setParkingStopTime(LocalDateTime.of(2019, 2, 1, 10, 15));
            p1.setStage(ParkingProcess.Stage.STOPPED_UNPAID);
            p1.setPrimaryCurrencyCost(new Money(Currency.getInstance("PLN"), new BigDecimal("10.12345")));
            repository.save(p1);

            ParkingProcess p2 = new ParkingProcess();
            p2.setMeterId(200);
            p2.setParkingStartTime(LocalDateTime.of(2019, 2, 1, 6, 15));
            p2.setParkingStopTime(LocalDateTime.of(2019, 2, 1, 10, 16));
            p2.setStage(ParkingProcess.Stage.STOPPED_UNPAID);
            p2.setPrimaryCurrencyCost(new Money(Currency.getInstance("PLN"), new BigDecimal("31.33")));
            repository.save(p2);
        };

    }
}
