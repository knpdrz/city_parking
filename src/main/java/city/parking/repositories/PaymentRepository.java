package city.parking.repositories;

import city.parking.entities.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findAllByDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
