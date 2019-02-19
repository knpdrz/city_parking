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
        LocalDateTime dayStartDate = LocalDateTime.of(day, LocalTime.of(0,0,0));
        LocalDateTime dayEndDate = LocalDateTime.of(day, LocalTime.of(23,59, 59));

        List<Payment> payments = paymentRepository.findAllByDateBetween(dayStartDate, dayEndDate);
        double dailyProfit = 0;
        for(Payment payment : payments){
            dailyProfit += payment.getCost();
        }
        return dailyProfit;
    }
}
