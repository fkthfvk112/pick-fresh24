package mart.fresh.com.service;

import java.util.List;

import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.dto.ProductInfoDto;
import mart.fresh.com.data.dto.ProductProcessResult;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.Member;

public interface CartService {
	List<CartInfoDto> getCartInfo(String memberId);
    String addToCart(String memberId, String productName, int storeId, int requestQuantity);
	void saveCart(Cart cart);
	int getMyCartStoreId(String memberId);
	Cart findByMember(Member member);
	String recoverStoreProductQuantity(String memberId, ProductProcessResult productProcessResult);
	ProductProcessResult decreaseStoretProductQuantity(String memberId, List<ProductInfoDto> productInfoList);
	ProductProcessResult decreaseCartProductQuantity(String memberId, List<ProductInfoDto> productInfoList);
}
