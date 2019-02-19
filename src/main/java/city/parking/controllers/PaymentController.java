package city.parking.controllers;

import city.parking.entities.Payment;
import city.parking.services.PaymentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RestController
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    @RequestMapping(path = "/payments", method = RequestMethod.GET)
    public List<Payment> getAllPayments(){
        return paymentService.findAllPayments();
    }

    @RequestMapping(path = "/payments", method = RequestMethod.POST)
    public Payment makePayment(@RequestBody Payment payment){
        return paymentService.makePayment(payment);
    }


    @RequestMapping(path = "/profits", method = RequestMethod.GET)
    public Double getDailyProfit(@RequestParam("day") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate day){
        return paymentService.getDailyProfit(day);
    }
}
