package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class CartProductDto {
    private String productTitle;  // 제품 이름
    private int priceNumber;      // 제품 가격
    private int cartProductQuantity;  // 장바구니에 담긴 제품 수량
}