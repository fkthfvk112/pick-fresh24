package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class CartDto {
    private int cartId;
    private MypageDto memberId;
    private ProductDto product;
    private StoreDto store;
}