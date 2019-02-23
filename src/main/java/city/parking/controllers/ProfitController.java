package city.parking.controllers;

import city.parking.entities.Money;
import city.parking.services.ProfitService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Collection;

@RestController
@RequestMapping("/profits")
public class ProfitController {
    private final ProfitService profitService;
    public ProfitController(ProfitService profitService){
        this.profitService = profitService;
    }

    @GetMapping
    public ResponseEntity<Collection<Money>> getDailyProfit(@RequestParam("day") @DateTimeFormat(pattern="yyyy-MM-dd") LocalDate day){
        return ResponseEntity.ok(profitService.getDailyProfit(day));
    }
}
