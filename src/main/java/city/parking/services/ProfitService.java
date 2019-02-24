package city.parking.services;

import city.parking.entities.Money;
import city.parking.entities.Payment;
import city.parking.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collection;
import java.util.Currency;
import java.util.HashMap;
import java.util.List;

@Service
public class ProfitService {
    private static final LocalTime FIRST_HOUR_IN_DAY = LocalTime.of(0, 0, 0);
    private static final LocalTime LAST_HOUR_IN_DAY = LocalTime.of(23, 59, 59);
    private final PaymentRepository paymentRepository;

    public ProfitService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public Collection<Money> getDailyProfit(LocalDate day) {
        LocalDateTime dayStartDate = LocalDateTime.of(day, FIRST_HOUR_IN_DAY);
        LocalDateTime dayEndDate = LocalDateTime.of(day, LAST_HOUR_IN_DAY);

        List<Payment> payments = paymentRepository.findAllByDateBetween(dayStartDate, dayEndDate);
        return getPaidBalanceSummary(payments);
    }

    private Collection<Money> getPaidBalanceSummary(List<Payment> payments) {
        Money balancePaid, moneyEntry;
        HashMap<Currency, Money> profitsMap = new HashMap<>();

        for (Payment payment : payments) {
            balancePaid = payment.getBalancePaid();
            if (profitsMap.containsKey(balancePaid.getCurrency())) {
                moneyEntry = profitsMap.get(balancePaid.getCurrency());
                moneyEntry.setAmount(moneyEntry.getAmount().add(balancePaid.getAmount()));
                profitsMap.put(moneyEntry.getCurrency(), moneyEntry);
            } else {
                profitsMap.put(balancePaid.getCurrency(), balancePaid);
            }
        }
        return profitsMap.values();
    }
}
