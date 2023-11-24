package mart.fresh.com.data.dto;

import lombok.Data;
import lombok.Getter;
import mart.fresh.com.error.ErrorCode;
import mart.fresh.com.error.exception.CartProductException;

@Data
@Getter
public class CartInfoDto {
	private int cartId;
	protected int cartProductId;
    private int productId;
    private String productTitle;
    private String productImgUrl;
    private int priceNumber;
    private int productTimeSale;
    protected int cartProductQuantity;
    private int storeId;
    private String storeName;

    public CartInfoDto(int cartId, int cartProductId, int productId, String productTitle, String productImgUrl, int priceNumber, int productTimeSale, int cartProductQuantity, int storeId, String storeName) {
        this.cartId = cartId;
        this.cartProductId = cartProductId;
        this.productId = productId;
    	this.productTitle = productTitle;
    	this.productImgUrl = productImgUrl;
        this.priceNumber = priceNumber;
        this.productTimeSale = productTimeSale;
        this.cartProductQuantity = cartProductQuantity;
        this.storeId = storeId;
        this.storeName = storeName;
    }
    
    public CartInfoDto(int cartProductId, int cartProductQuantity) {
        this.cartProductId = cartProductId;
        this.cartProductQuantity = cartProductQuantity;
    }
    
    public void outOfStock() {
    	if(this.cartProductQuantity <= 0) {
    		throw new CartProductException(ErrorCode.CART_PRODUCT_OUT_OF_STOCK);
    	}
    }
}

