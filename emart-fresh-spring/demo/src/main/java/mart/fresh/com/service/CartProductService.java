package mart.fresh.com.service;

import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;

public interface CartProductService {
  void removeProductFromCart(String memberId, int cartProductId);
	boolean updateCartProductQuantity(String memberId, int cartProductId, int cartProductQuantity);
	CartProduct findByCart(Cart cart);
	void removeAllProductsFromCart(int cartId);
}
