package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class PaymentCancelDto {
	private String receiptId;
    private String orderId;
}
