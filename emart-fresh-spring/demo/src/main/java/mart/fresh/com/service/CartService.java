package mart.fresh.com.service;

import java.util.List;

import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.entity.Cart;

public interface CartService {
	List<CartInfoDto> getCartInfo(String memberId);
  String addToCart(String memberId, String productName, int storeId, int requestQuantity);
	String decreaseCartProductQuantity(String memberId);
	void saveCart(Cart cart);
	int getMyCartStoreId(String memberId);
}
