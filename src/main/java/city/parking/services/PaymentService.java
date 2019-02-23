package city.parking.services;

import city.parking.entities.ParkingProcess;
import city.parking.entities.Payment;
import city.parking.exceptions.ParkingProcessNotFoundException;
import city.parking.repositories.ParkingProcessRepository;
import city.parking.repositories.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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
        if(processOptional.isPresent()){
            ParkingProcess process = processOptional.get();
            setPaymentDetails(payment, process);
            return payment;
        }else {
            throw new ParkingProcessNotFoundException(payment.getParkingProcessId());
        }
    }

    private void setPaymentDetails(Payment payment, ParkingProcess process){
        payment.setBalancePaid(process.getPrimaryCurrencyCost());
        payment.setDate(LocalDateTime.now());
        process.setStage(ParkingProcess.Stage.PAID);
        parkingProcessRepository.save(process);
        paymentRepository.save(payment);
    }
}
