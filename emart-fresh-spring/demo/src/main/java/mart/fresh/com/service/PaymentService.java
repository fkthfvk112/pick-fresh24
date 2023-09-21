package mart.fresh.com.service;

import java.util.Optional;

import mart.fresh.com.data.dto.PaymentInfoDTO;

public interface PaymentService {

	void processPaymentInfo(PaymentInfoDTO paymentInfoDto);
	void updatePaymentStatusLocale(String receiptId);
	Optional<String> findReceiptIdByOrderId(String orderId);


}
