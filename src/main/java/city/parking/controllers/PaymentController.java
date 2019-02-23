package city.parking.controllers;

import city.parking.entities.Payment;
import city.parking.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments(){
        return ResponseEntity.ok(paymentService.findAllPayments());
    }

    @PostMapping
    public ResponseEntity<Payment> makePayment(@RequestBody Payment payment){
        return ResponseEntity.ok(paymentService.makePayment(payment));
    }
}
