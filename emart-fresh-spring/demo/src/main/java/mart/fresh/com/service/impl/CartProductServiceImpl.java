package mart.fresh.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.CartProductDao;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;
import mart.fresh.com.service.CartProductService;

@Service
public class CartProductServiceImpl implements CartProductService{
	private final CartProductDao cartProductDao;
	
	@Autowired
	public CartProductServiceImpl(CartProductDao cartProductDao) {
		this.cartProductDao = cartProductDao;
	}

	@Override
	public void removeProductFromCart(String memberId, int cartProductId) {
		cartProductDao.removeProductFromCart(memberId, cartProductId);
	}
	@Override
	public boolean updateCartProductQuantity(String memberId, int cartProductId, int cartProductQuantity) {
		return cartProductDao.updateCartProductQuantity(memberId, cartProductId, cartProductQuantity);
	}

	@Override
	public CartProduct findByCart(Cart cart) {
		return cartProductDao.findByCart(cart);
	}

	@Override
	public void removeAllProductsFromCart(int cartId) {
		cartProductDao.removeAllProductsFromCart(cartId);
	}
}
