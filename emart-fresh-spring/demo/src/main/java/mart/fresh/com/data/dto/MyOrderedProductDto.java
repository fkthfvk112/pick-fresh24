package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class MyOrderedProductDto {
	private String memberId;
    private int productId;
    private int storeId;
    private int couponId;
    private boolean isPickup;
    private String storeName;
    private Timestamp orderedDate;
    private String productTitle;
    private String orderCode;
    private int orderedQuantity;
    private int totalAmount;
    private int myOrderedCount;
    private String productImgUrl;
    private int orderedProductId;
    private String audioData;
}