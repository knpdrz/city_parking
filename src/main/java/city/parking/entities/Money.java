package city.parking.entities;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Currency;

@Data
@Setter
@NoArgsConstructor
public class Money {
    private Currency currency;
    private double amount;

    public Money(Currency currency, double amount){
        this.currency = currency;
        this.amount = amount;
    }
}
