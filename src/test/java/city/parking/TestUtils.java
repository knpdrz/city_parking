package city.parking;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Currency;

class TestUtils {
    static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static ParkingProcess prepareDummyParkingProcess(LocalDateTime parkingStartTime, LocalDateTime parkingStopTime,
                                                     ParkingProcess.Stage stage, boolean forDisabled) {
        ParkingProcess process = new ParkingProcess();
        process.setMeterId(5);
        process.setForDisabled(forDisabled);
        process.setParkingStartTime(parkingStartTime);
        process.setParkingStopTime(parkingStopTime);
        process.setStage(stage);
        process.setPrimaryCurrencyCost(new Money(Currency.getInstance("PLN"), new BigDecimal("0")));
        return process;
    }
}
