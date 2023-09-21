package mart.fresh.com.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import mart.fresh.com.data.entity.Payment;

public interface PaymentRepository extends JpaRepository<Payment, String> {

	Optional<Payment> findByReceiptId(String receiptId);
	Optional<Payment> findByOrderId(String orderId);
	

}
