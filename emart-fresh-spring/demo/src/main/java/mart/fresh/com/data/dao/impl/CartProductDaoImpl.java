package mart.fresh.com.data.dao.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import mart.fresh.com.data.dao.CartProductDao;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;
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
	public boolean removeProductFromCart(String memberId, int cartProductId) {
	    int result = cartProductRepository.deleteByCartMemberMemberIdAndCartProductId(memberId, cartProductId);
	    return result > 0;
	}

	@Override
	public boolean updateCartProductQuantity(String memberId, int cartProductId, int cartProductQuantity) {
        Optional<CartProduct> cartProductOptional = cartProductRepository.findById(cartProductId);
        
        if (cartProductOptional.isPresent()) {
            CartProduct cartProduct = cartProductOptional.get();
            
            if (!cartProduct.getCart().getMember().getMemberId().equals(memberId)) {
                throw new IllegalArgumentException(memberId+"의 장바구니와 수량을 바꿀려는 장바구니가 다릅니다.");
            }
            
            cartProduct.setCartProductQuantity(cartProductQuantity);
            cartProductRepository.save(cartProduct);
            return true;
        } else {
            throw new EntityNotFoundException("해당 물품이 장바구니에 없습니다. : " + cartProductId);
        }
	}

	@Override
	public CartProduct findByCart(Cart cart) {
		return cartProductRepository.findByCart(cart);
	}

	@Override
	@Transactional
	public void removeAllProductsFromCart(int cartId) {
		cartProductRepository.deleteByCart_CartId(cartId);
		
	}

	@Override
	public List<CartProduct> findListByCart(Cart cart) {
		int cartId = cart.getCartId();
		return cartProductRepository.findByCartCartId(cartId);
	}

}
