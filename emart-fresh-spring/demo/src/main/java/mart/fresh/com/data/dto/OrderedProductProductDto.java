package mart.fresh.com.data.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderedProductProductDto {
	private int orderedQuantity;
    private int productId;
    private int orderedProductId;
    private String productTitle;
    private int price;
    
    @Builder
	public OrderedProductProductDto(int orderedQuantity, int productId, int orderedProductId, String productTitle,
			int price) {
		super();
		this.orderedQuantity = orderedQuantity;
		this.productId = productId;
		this.orderedProductId = orderedProductId;
		this.productTitle = productTitle;
		this.price = price;
	}
    
    
}
