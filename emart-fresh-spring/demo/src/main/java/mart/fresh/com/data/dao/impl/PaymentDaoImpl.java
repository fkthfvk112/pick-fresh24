package mart.fresh.com.data.dao.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import mart.fresh.com.data.dao.PaymentDao;
import mart.fresh.com.data.dto.PaymentInfoDTO;
import mart.fresh.com.data.entity.Payment;
import mart.fresh.com.data.repository.PaymentRepository;

@Component
public class PaymentDaoImpl implements PaymentDao {
	private final PaymentRepository paymentRepository;

	@Autowired
	public PaymentDaoImpl(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}

	@Override
	public void savePaymentInfo(PaymentInfoDTO paymentInfoDTO) {

		Payment payment = new Payment();

		payment.setReceiptId(paymentInfoDTO.getReceiptId());
		payment.setOrderId(paymentInfoDTO.getOrderId());
        payment.setPg(paymentInfoDTO.getPg());
        payment.setMethodOrigin(paymentInfoDTO.getMethodOrigin());
        payment.setMethod(paymentInfoDTO.getMethod());
        payment.setStatusLocale(paymentInfoDTO.getStatusLocale());
        payment.setReceiptUrl(paymentInfoDTO.getReceiptUrl());
        payment.setOrderedProductId(paymentInfoDTO.getOrderedProductId());

		paymentRepository.save(payment);
	}

	@Override
    @Transactional
	public void updatePaymentStatusLocale(String receiptId) {
		Optional<Payment> optionalPayment = paymentRepository.findByReceiptId(receiptId);

        if (optionalPayment.isPresent()) {
            Payment payment = optionalPayment.get();
            payment.setStatusLocale("결제취소");
            paymentRepository.save(payment); // 엔터티 수정 후 저장
        } else {
        	System.out.println("해당 receiptId로 DB에서 찾을 수 없음 !!");
        	System.out.println("receiptId : " + receiptId);
        }
	}
	
	@Override
    public Optional<String> findReceiptIdByOrderId(String orderId) {
        Optional<Payment> paymentOptional = paymentRepository.findByOrderId(orderId);
        return paymentOptional.map(Payment::getReceiptId);
    }
	
}