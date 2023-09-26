package mart.fresh.com.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import mart.fresh.com.data.dto.PaymentCancelDto;
import mart.fresh.com.data.dto.PaymentInfoDTO;
import mart.fresh.com.service.PaymentService;

@RestController
@RequestMapping("/payment")
public class PaymentController {
	private final PaymentService paymentService;
	
	@Autowired
	public PaymentController(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@Value("${bootpay.application-id}")
    private String BOOTPAY_APPLICATION_ID;

    @Value("${bootpay.private-key}")
    private String BOOTPAY_PRIVATE_KEY;
    
    
    @PostMapping("/paymentInfo")
    public ResponseEntity<String> paymentInfo(@RequestBody PaymentInfoDTO paymentInfoDto) {
    	System.out.println("PaymentController 결제한 물품 정보 저장 " + new Date());
        try {
            System.out.println("프론트에서 결제 정보: " + paymentInfoDto);

            paymentService.processPaymentInfo(paymentInfoDto);
            
            return ResponseEntity.ok("결제 정보를 성공적으로 받았습니다.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("예외 에러 : " + e.getMessage());
        }
    }

	@PostMapping("/cancel")
    public ResponseEntity<?> cancelPayment(@RequestBody PaymentCancelDto paymentCancelDto) {
    	System.out.println("PaymentController 결제 취소 " + new Date());
        try {
            Bootpay bootpay = new Bootpay(BOOTPAY_APPLICATION_ID, BOOTPAY_PRIVATE_KEY);
            HashMap<String, Object> token = bootpay.getAccessToken();

            if (token.get("error_code") != null) {
            	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to get access token");
            }
            
            Optional<String> receiptIdOptional = paymentService.findReceiptIdByOrderId(paymentCancelDto.getOrderId());
            String receiptIdDB = receiptIdOptional.orElse(null);

            if (receiptIdDB != null && receiptIdDB.equals(paymentCancelDto.getReceiptId())) {
                Cancel cancel = new Cancel();
                cancel.receiptId = paymentCancelDto.getReceiptId();

                HashMap<String, Object> res = bootpay.receiptCancel(cancel);

                if (res.get("error_code") == null) {
                    System.out.println("결제 취소 성공 ! : " + res);
                    paymentService.updatePaymentStatusLocale(paymentCancelDto.getReceiptId());
                    return ResponseEntity.ok("결제 취소 성공 : " + res);
                } else {
                    System.out.println("결제 취소 실패 ! : " + res);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 취소 실패 ! : " + res);
                }
            } else {
                System.out.println("DB의 정보와 일치하지 않습니다.");
                return ResponseEntity.badRequest().body("orderId와 receiptId를 다시 확인해주세요.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("결제 취소 예외 발생 : " + e.getMessage());
        }
    }
}
