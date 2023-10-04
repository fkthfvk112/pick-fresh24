package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class OrderedProductProductDto {
	private int orderedQuantity;
    private int productId;
    private int orderedProductId;
    private String productTitle;
    private int price;
}
