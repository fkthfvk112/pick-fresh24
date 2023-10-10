package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class CartInfoDto {
	private int cartId;
	private int cartProductId;
    private int productId;
    private String productTitle;
    private String productImgUrl;
    private int priceNumber;
    private int productTimeSale;
    private int cartProductQuantity;
    private int storeId;

    public CartInfoDto(int cartId, int cartProductId, int productId, String productTitle, String productImgUrl, int priceNumber, int productTimeSale, int cartProductQuantity, int storeId) {
        this.cartId = cartId;
        this.cartProductId = cartProductId;
        this.productId = productId;
    	this.productTitle = productTitle;
    	this.productImgUrl = productImgUrl;
        this.priceNumber = priceNumber;
        this.productTimeSale = productTimeSale;
        this.cartProductQuantity = cartProductQuantity;
        this.storeId = storeId;
    }
}

