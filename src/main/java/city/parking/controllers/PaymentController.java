package city.parking.controllers;

import city.parking.entities.Money;
import city.parking.entities.Payment;
import city.parking.services.PaymentService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<List<Payment>> getAllPayments(){
        return ResponseEntity.ok(paymentService.findAllPayments());
    }

    @PostMapping(path = "/payments")
    public ResponseEntity<Payment> makePayment(@RequestBody Payment payment){
        return ResponseEntity.ok(paymentService.makePayment(payment));
    }

    @GetMapping(path = "/profits")
    public ResponseEntity<Collection<Money>> getDailyProfit(@RequestParam("day") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate day){
        return ResponseEntity.ok(paymentService.getDailyProfit(day));
    }
}
