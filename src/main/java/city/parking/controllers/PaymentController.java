package city.parking.controllers;

import city.parking.entities.Money;
import city.parking.entities.Payment;
import city.parking.services.PaymentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping(path = "/payments")
    public List<Payment> getAllPayments(){
        return paymentService.findAllPayments();
    }

    @PostMapping(path = "/payments")
    public Payment makePayment(@RequestBody Payment payment){
        return paymentService.makePayment(payment);
    }

    @GetMapping(path = "/profits")
    public Collection<Money> getDailyProfit(@RequestParam("day") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate day){
        return paymentService.getDailyProfit(day);
    }
}
