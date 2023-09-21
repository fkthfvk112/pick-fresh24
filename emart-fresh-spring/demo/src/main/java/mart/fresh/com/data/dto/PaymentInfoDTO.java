package mart.fresh.com.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import mart.fresh.com.data.entity.Payment;

@Data
public class PaymentInfoDTO {
	private String receiptId;
    private String orderId;
    private String pg;
    private String methodOrigin;
    private String method;
    private String statusLocale;
    private String receiptUrl;
    
    private int orderedProductId;
    
    public Payment PaymentEntity() { // dto > entity
    	Payment payment = new Payment();
    	payment.setReceiptId(this.receiptId);
    	payment.setOrderId(this.orderId);
        payment.setPg(this.pg);
        payment.setMethodOrigin(this.methodOrigin);
        payment.setMethod(this.method);
        payment.setStatusLocale(this.statusLocale);
        payment.setReceiptUrl(this.receiptUrl);
        payment.setOrderedProductId(this.orderedProductId);
        
        return payment;
    }
}
