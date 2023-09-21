package mart.fresh.com.data.dao.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.CartDao;
import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.dto.CartProductDto;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;
import mart.fresh.com.data.repository.CartProductRepository;
import mart.fresh.com.data.repository.CartRepository;

@Component
public class CartDaoImpl implements CartDao {
    private final CartRepository cartRepository;

    @Autowired
    public CartDaoImpl(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    @Override
    public List<CartInfoDto> getCartInfo(String memberId) {
    	return cartRepository.getCartInfoByMemberId(memberId);
    }
	
	
}
