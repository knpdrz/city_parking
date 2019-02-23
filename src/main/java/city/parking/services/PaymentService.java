package city.parking.services;

import city.parking.entities.Money;
import city.parking.entities.ParkingProcess;
import city.parking.entities.Payment;
import city.parking.repositories.ParkingProcessRepository;
import city.parking.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;

@Service
public class PaymentService {
    private static final LocalTime FIRST_HOUR_IN_DAY = LocalTime.of(0,0,0);
    private static final LocalTime LAST_HOUR_IN_DAY = LocalTime.of(23,59, 59);

    private final ParkingProcessRepository parkingProcessRepository;
    private final PaymentRepository paymentRepository;

    public PaymentService(ParkingProcessRepository parkingProcessRepository,
                          PaymentRepository paymentRepository){
        this.parkingProcessRepository = parkingProcessRepository;
        this.paymentRepository = paymentRepository;
    }

    public List<Payment> findAllPayments(){
        return paymentRepository.findAll();
    }

    public Payment makePayment(Payment payment) {
        Optional<ParkingProcess> processOptional = parkingProcessRepository.findById(payment.getParkingProcessId());
        processOptional.ifPresent(process -> {
            payment.setBalancePaid(process.getPrimaryCurrencyCost());
            payment.setDate(LocalDateTime.now());
            process.setStage(ParkingProcess.Stage.PAID);
            parkingProcessRepository.save(process);
            paymentRepository.save(payment);
        });
        paymentRepository.save(payment);
        return payment;
    }

    public Collection<Money> getDailyProfit(LocalDate day) {
        LocalDateTime dayStartDate = LocalDateTime.of(day, FIRST_HOUR_IN_DAY);
        LocalDateTime dayEndDate = LocalDateTime.of(day, LAST_HOUR_IN_DAY);

        List<Payment> payments = paymentRepository.findAllByDateBetween(dayStartDate, dayEndDate);
        Money balancePaid;

        HashMap<Currency, Money> profitsMap = new HashMap<>();
        Money moneyEntry;

        for(Payment payment : payments){
            balancePaid = payment.getBalancePaid();
            if(profitsMap.containsKey(balancePaid.getCurrency())){
                moneyEntry = profitsMap.get(balancePaid.getCurrency());
                moneyEntry.setAmount(moneyEntry.getAmount() + balancePaid.getAmount());
                profitsMap.put(moneyEntry.getCurrency(), moneyEntry);
            }else{
                profitsMap.put(balancePaid.getCurrency(), balancePaid);
            }

        }
        return profitsMap.values();
    }
}
