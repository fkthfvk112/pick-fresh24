package mart.fresh.com.service;

import java.util.List;

import mart.fresh.com.data.dto.CartUpdateInfoDto;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;

public interface CartProductService {
	boolean removeProductFromCart(String memberId, int cartProductId);

	CartProduct findByCart(Cart cart);
	List<CartProduct> findListByCart(Cart cart);

	void removeAllProductsFromCart(int cartId);

	boolean updateCartProductQuantity(String memberId, List<CartUpdateInfoDto> requestItems);
}
