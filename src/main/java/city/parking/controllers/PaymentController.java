package city.parking.controllers;

import city.parking.entities.Payment;
import city.parking.services.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(path = "/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @RequestMapping(method = RequestMethod.POST)
    public Payment startParkingProcess(@RequestBody Payment payment){
        return paymentService.makePayment(payment);
    }
}
