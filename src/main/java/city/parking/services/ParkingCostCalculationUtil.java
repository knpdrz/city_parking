package city.parking.services;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Currency;

public class ParkingCostCalculationUtil {
    private static final double REGULAR_COST_MULTIPLIER = 1.5;
    private static final double COST_FOR_DISABLED_MULTIPLIER = 1.2;
    private static final Currency PRIMARY_CURRENCY = Currency.getInstance("PLN");
    private static final int ROUNDING_SCALE = 2; //number of decimal places of money amount to keep

    public static Currency getPrimaryCurrency(){
        return PRIMARY_CURRENCY;
    }

    public static Money calculatePrimaryParkingCost(ParkingProcess process) {
        int parkingTimeInHours = getParkingTimeInHours(process);
        double primaryCost = process.isForDisabled() ? getDisabledCost(parkingTimeInHours) : getRegularCost(parkingTimeInHours);
        BigDecimal scaledPrimaryCost = new BigDecimal(primaryCost);
        scaledPrimaryCost = scaledPrimaryCost.setScale(ROUNDING_SCALE, RoundingMode.HALF_EVEN);
        return new Money(PRIMARY_CURRENCY, scaledPrimaryCost);
    }

    private static int getParkingTimeInHours(ParkingProcess process) {
        LocalDateTime startTime = process.getParkingStartTime();
        LocalDateTime stopTime = process.getParkingStopTime();
        long parkingTimeInMillis = Duration.between(startTime, stopTime).toMillis();
        long millisInHour = Duration.ofHours(1).toMillis();

        int parkingTimeInHours = (int) Math.ceil((double) parkingTimeInMillis / millisInHour);
        return parkingTimeInHours;
    }

    private static double getRegularCost(int hoursPassed) {
        if (hoursPassed < 1) return 0;
        else if (hoursPassed == 1) return 1;
        else return 1 + 2 * (1 - Math.pow(REGULAR_COST_MULTIPLIER, hoursPassed - 1)) / (1 - REGULAR_COST_MULTIPLIER);
    }

    private static double getDisabledCost(int hoursPassed) {
        if (hoursPassed <= 1) return 0;
        else {
            return 2 * (1 - Math.pow(COST_FOR_DISABLED_MULTIPLIER, hoursPassed - 1)) / (1 - COST_FOR_DISABLED_MULTIPLIER);
        }
    }
}
