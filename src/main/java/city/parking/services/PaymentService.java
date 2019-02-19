package city.parking.services;

import city.parking.entities.ParkingProcess;
import city.parking.entities.Payment;
import city.parking.repositories.ParkingProcessRepository;
import city.parking.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

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
            payment.setCost(process.getCost());
            payment.setDate(LocalDateTime.now());
            paymentRepository.save(payment);
        });
        paymentRepository.save(payment);
        return payment;
    }


    public Double getDailyProfit(LocalDate day) {
        LocalDateTime dayStartDate = LocalDateTime.of(day, FIRST_HOUR_IN_DAY);
        LocalDateTime dayEndDate = LocalDateTime.of(day, LAST_HOUR_IN_DAY);

        List<Payment> payments = paymentRepository.findAllByDateBetween(dayStartDate, dayEndDate);
        double dailyProfit = 0;
        for(Payment payment : payments){
            dailyProfit += payment.getCost();
        }
        return dailyProfit;
    }
}
