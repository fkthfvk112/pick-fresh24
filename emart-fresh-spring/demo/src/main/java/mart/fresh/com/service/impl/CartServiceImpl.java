package mart.fresh.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.CartDao;
import mart.fresh.com.data.dao.CartProductDao;
import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.dto.ProductInfoDto;
import mart.fresh.com.data.dto.ProductProcessResult;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.service.CartService;

@Service
public class CartServiceImpl implements CartService {
	private final CartDao cartDao;

	@Autowired
	public CartServiceImpl(CartDao cartDao) {
		this.cartDao = cartDao;
	}

	@Override
	public List<CartInfoDto> getCartInfo(String memberId) {
		return cartDao.getCartInfo(memberId);
	}

	@Override
	public String addToCart(String memberId, String productName, int storeId, int requestQuantity) {
		return cartDao.addToCart(memberId, productName, storeId, requestQuantity);
	}

	@Override
	public String decreaseCartProductQuantity(String memberId) {
		return cartDao.decreaseCartProductQuantity(memberId);
	}

	@Override
	public void saveCart(Cart cart) {
		cartDao.saveCart(cart);
	}

	public int getMyCartStoreId(String memberId) {
		return cartDao.getMyCartStoreId(memberId);
	}

	@Override
	public Cart findByMember(Member member) {
		return cartDao.findByMember(member);
	}

	@Override
	public String recoverCartProductQuantity(String memberId, ProductProcessResult productProcessResult) {
		return cartDao.recoverCartProductQuantity(memberId, productProcessResult);
	}

	@Override
	public ProductProcessResult decreaseStoretProductQuantity(String memberId, List<ProductInfoDto> productInfoList) {
		return cartDao.decreaseStoretProductQuantity(memberId, productInfoList);
		
	}

	@Override
	public ProductProcessResult decreaseCartProductQuantity(String memberId, List<ProductInfoDto> productInfoList) {
		return cartDao.decreaseCartProductQuantity(memberId, productInfoList);
	}

}
