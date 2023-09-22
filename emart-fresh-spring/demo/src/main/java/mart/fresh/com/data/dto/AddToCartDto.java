package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class AddToCartDto {
	int storeId;
	String productName;
	int requestQuantity;
}
