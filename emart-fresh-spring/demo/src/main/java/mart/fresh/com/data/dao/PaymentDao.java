package mart.fresh.com.data.dao;

import java.util.Optional;

import mart.fresh.com.data.dto.PaymentInfoDTO;

public interface PaymentDao {
	void savePaymentInfo(PaymentInfoDTO paymentInfoDTO);
    void updatePaymentStatusLocale(String receiptId);
	Optional<String> findReceiptIdByOrderId(String orderId);
}
