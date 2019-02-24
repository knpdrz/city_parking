package city.parking;

import city.parking.entities.Money;
import city.parking.entities.Payment;
import city.parking.repositories.PaymentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Currency;
import java.util.Random;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class ProfitControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PaymentRepository paymentRepository;

    @Test
    public void givenThreePayments_whenGetDailyProfit_thenGetTotalProfit() throws Exception {
        //given three payments made on the same day
        LocalDate day = LocalDate.of(2019, 2, 1);
        LocalTime time1 = LocalTime.of(11, 34);
        LocalTime time2 = LocalTime.of(10, 53);
        LocalTime time3 = LocalTime.of(15, 0);
        Currency currency = Currency.getInstance("PLN");
        Payment p1 = prepareDummyPayment(1, currency, new BigDecimal("6.4"), LocalDateTime.of(day, time1));
        Payment p2 = prepareDummyPayment(2, currency, new BigDecimal("7.23"), LocalDateTime.of(day, time2));
        Payment p3 = prepareDummyPayment(3, currency, new BigDecimal("91.99"), LocalDateTime.of(day, time3));
        paymentRepository.save(p1);
        paymentRepository.save(p2);
        paymentRepository.save(p3);

        //prepare date (day) parameter for GET request in correct format
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dayInApiFormat = day.format(formatter);

        //sum amounts from all payments to get expected earnings in one particular day
        BigDecimal expectedDayEarnings = p1.getBalancePaid().getAmount().add(p2.getBalancePaid().getAmount()).add(p3.getBalancePaid().getAmount());

        //prepare expected object with amount of money paid given day in selected currency
        Money expectedMoney = new Money();
        expectedMoney.setAmount(expectedDayEarnings);
        expectedMoney.setCurrency(currency);
        String expectedMoneyString = asJsonString(expectedMoney);

        //when a GET request is sent to /profits endpoint for daily profits
        mockMvc.perform(get("/profits")
                        .param("day", dayInApiFormat))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(expectedMoneyString)));
    }

    private Payment prepareDummyPayment(Integer paymentProcessId, Currency currency, BigDecimal amountPaid, LocalDateTime paymentDate){
        Payment payment = new Payment();
        payment.setParkingProcessId(paymentProcessId);
        payment.setBalancePaid(new Money(currency, amountPaid));
        payment.setDate(paymentDate);
        return payment;
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
