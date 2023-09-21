package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class OrderedProductDto {
	private String memberId;
    private int productId;
    private int storeId;
    private int orderedProductId;
    private int orderedQuantity;
    private boolean isPickup;
    private boolean orderedDel;
    private Timestamp orderedDate;
}