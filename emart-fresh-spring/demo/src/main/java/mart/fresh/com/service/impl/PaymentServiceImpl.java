package mart.fresh.com.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.PaymentDao;
import mart.fresh.com.data.dto.PaymentInfoDTO;
import mart.fresh.com.service.PaymentService;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentDao paymentDao;

    @Autowired
    public PaymentServiceImpl(PaymentDao paymentDao) {
        this.paymentDao = paymentDao;
    }

    @Override
    public void processPaymentInfo(PaymentInfoDTO paymentInfoDTO) {
        paymentDao.savePaymentInfo(paymentInfoDTO);
    }

	@Override
	public void updatePaymentStatusLocale(String receiptId) {
		paymentDao.updatePaymentStatusLocale(receiptId);
		
	}

	@Override
	public Optional<String> findReceiptIdByOrderId(String orderId) {
		return paymentDao.findReceiptIdByOrderId(orderId);
	}
}
