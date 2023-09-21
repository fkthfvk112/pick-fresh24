package mart.fresh.com.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import mart.fresh.com.data.dao.CartProductDao;
import mart.fresh.com.data.repository.CartProductRepository;

@Component
public class CartProductDaoImpl implements CartProductDao {
	private final CartProductRepository cartProductRepository;
	
	@Autowired
	public CartProductDaoImpl(CartProductRepository cartProductRepository) {
		this.cartProductRepository = cartProductRepository;
	}
	
	@Override
	@Transactional
	public void removeProductFromCart(String memberId, int cartProductId) {
        cartProductRepository.deleteByCartMemberMemberIdAndCartProductId(memberId, cartProductId);
	}
	

}
