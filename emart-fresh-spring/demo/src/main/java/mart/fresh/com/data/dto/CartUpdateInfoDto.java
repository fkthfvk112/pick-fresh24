package mart.fresh.com.data.dto;

import lombok.Getter;

@Getter
public class CartUpdateInfoDto extends CartInfoDto {
    public CartUpdateInfoDto(int cartProductId, int cartProductQuantity) {
    	super(cartProductId, cartProductQuantity);
    }
}
