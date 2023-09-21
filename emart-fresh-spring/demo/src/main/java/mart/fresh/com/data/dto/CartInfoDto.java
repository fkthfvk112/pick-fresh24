package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class CartInfoDto {
	private int cartId;
	private int cartProductId;
    private String productTitle;
    private int priceNumber;
    private int cartProductQuantity;
    private int storeId;

    public CartInfoDto(int cartId, int cartProductId, String productTitle, int priceNumber, int cartProductQuantity, int storeId) {
        this.cartId = cartId;
        this.cartProductId = cartProductId;
    	this.productTitle = productTitle;
        this.priceNumber = priceNumber;
        this.cartProductQuantity = cartProductQuantity;
        this.storeId = storeId;
    }
}

