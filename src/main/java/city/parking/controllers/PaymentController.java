package city.parking.controllers;

import city.parking.entities.ParkingProcess;
import city.parking.entities.Payment;
import city.parking.services.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.Optional;

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

    @GetMapping(value = "/{paymentId}")
    public ResponseEntity<Payment> getPayment(@PathVariable Integer paymentId){
        Optional<Payment> paymentOptional = paymentService.findById(paymentId);
        if(paymentOptional.isPresent()){
            return ResponseEntity.ok(paymentOptional.get());
        }else{
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Payment> makePayment(UriComponentsBuilder ucBuilder,
                                               @Valid @RequestBody Payment payment){
        Payment newPayment = paymentService.makePayment(payment);
        URI newPaymentLocationURI = ucBuilder.path("/payments/{paymentId}")
                .buildAndExpand(newPayment.getId()).toUri();

        return ResponseEntity.created(newPaymentLocationURI).body(newPayment);
    }
}
