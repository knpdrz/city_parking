package city.parking.services;

import city.parking.entities.ParkingProcess;
import city.parking.entities.Payment;
import city.parking.repositories.ParkingProcessRepository;
import city.parking.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
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

    public Payment makePayment(Payment payment) {
        Optional<ParkingProcess> processOptional = parkingProcessRepository.findById(payment.getParkingProcessId());
        processOptional.ifPresent(process -> {

        });
        //todo- get parking process and amount in it and save in payment and add to some global repo? this <day, earnings>
        paymentRepository.save(payment);
        return payment;
    }



}
