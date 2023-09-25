package mart.fresh.com.service;

import java.util.List;

import mart.fresh.com.data.dto.CartInfoDto;

public interface CartService {
	List<CartInfoDto> getCartInfo(String memberId);
    String addToCart(String memberId, String productName, int storeId, int requestQuantity);

}
