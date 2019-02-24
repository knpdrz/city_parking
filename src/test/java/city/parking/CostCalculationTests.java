package city.parking;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.services.ParkingCostCalculationUtil;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

import static city.parking.TestUtils.prepareDummyParkingProcess;
import static org.junit.Assert.assertEquals;

public class CostCalculationTests {
    private static final double REGULAR_COST_MULTIPLIER = 1.5;
    private static final double COST_FOR_DISABLED_MULTIPLIER = 1.2;

    @Test
    public void disabledCostCalculationTest() {
        int hoursDiff = 5;
        LocalDateTime parkingStopTime = LocalDateTime.now();
        LocalDateTime parkingStartTime = parkingStopTime.minusHours(hoursDiff);

        ParkingProcess process = prepareDummyParkingProcess(parkingStartTime, parkingStopTime, ParkingProcess.Stage.STOPPED_UNPAID, true);
        Money actualMoney = ParkingCostCalculationUtil.calculatePrimaryParkingCost(process);
        BigDecimal actualCost = actualMoney.getAmount();

        BigDecimal expectedCost = getParkingCostForDisabledIterative(hoursDiff).setScale(2, RoundingMode.HALF_EVEN);

        assertEquals(expectedCost, actualCost);
    }

    @Test
    public void regularCostCalculationTest() {
        int hoursDiff = 5;
        LocalDateTime parkingStopTime = LocalDateTime.now();
        LocalDateTime parkingStartTime = parkingStopTime.minusHours(hoursDiff);

        ParkingProcess process = prepareDummyParkingProcess(parkingStartTime, parkingStopTime, ParkingProcess.Stage.STOPPED_UNPAID, false);
        Money actualMoney = ParkingCostCalculationUtil.calculatePrimaryParkingCost(process);
        BigDecimal actualCost = actualMoney.getAmount();

        BigDecimal expectedCost = getRegularParkingCostIterative(hoursDiff).setScale(2, RoundingMode.HALF_EVEN);

        assertEquals(expectedCost, actualCost);
    }

    private BigDecimal getParkingCostForDisabledIterative(int hoursDiff) {
        double cost = 0, currentHourCost = 2;

        for (int hour = 2; hour <= hoursDiff; hour++) {
            cost += currentHourCost;
            currentHourCost *= COST_FOR_DISABLED_MULTIPLIER;
        }
        return new BigDecimal(cost);
    }

    private BigDecimal getRegularParkingCostIterative(int hoursDiff) {
        double cost, currentHourCost;
        if (hoursDiff <= 1) return new BigDecimal("1");

        currentHourCost = 2;
        cost = 1;
        for (int hour = 2; hour <= hoursDiff; hour++) {
            cost += currentHourCost;
            currentHourCost *= REGULAR_COST_MULTIPLIER;
        }
        return new BigDecimal(cost);
    }
}
