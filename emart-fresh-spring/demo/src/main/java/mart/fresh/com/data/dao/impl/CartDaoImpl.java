package mart.fresh.com.data.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.CartDao;
import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.dto.CartProductDto;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.entity.StoreProduct;
import mart.fresh.com.data.repository.CartProductRepository;
import mart.fresh.com.data.repository.CartRepository;
import mart.fresh.com.data.repository.MemberRepository;
import mart.fresh.com.data.repository.StoreRepository;

@Component
public class CartDaoImpl implements CartDao {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final CartProductRepository cartProductRepository;
    @Autowired
    public CartDaoImpl(CartRepository cartRepository, MemberRepository memberRepository,
    		StoreRepository storeRepository, CartProductRepository cartProductRepository) {
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.storeRepository = storeRepository;
        this.cartProductRepository = cartProductRepository;
    }

    @Override
    public List<CartInfoDto> getCartInfo(String memberId) {
    	return cartRepository.getCartInfoByMemberId(memberId);
    }

    
    //한 번에 총 수량 넘는 만큼 추가하면 error
    //하지만 장바구니에 나누어서 담아서 수량 추가하는건 ok하도록 로직 구현함
	@Override
	public String addToCart(String memberId, String productName, int storeId, int requestQuantity) {
		
		//내 카트 받아오기
		List<Cart> cartList =  cartRepository.findByMemberMemberId(memberId);

		Cart cart = cartList.get(0);

		if(cart == null) return "장바구니 테이블 생성 안 되어있음";
		
		//다른 가게가 카트에 이미 존재하면 에러 :
		//0은 빈 상태, 즉 에러 X
		if(cart.getStore().getStoreId() != 0 && cart.getStore().getStoreId() != storeId) return "error:another store exist";
		
		System.out.println("-------------adst : ");
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		List<StoreProduct> storeProductList = cartRepository.getAvailableProducts(storeId, productName, currentTimestamp);
		System.out.println("-------------addToCart productList : ");
		
		//해당 상품의 총 개수를 구함
		if(storeProductList == null || storeProductList.size() <= 0) return "error:out of stock";
		int sumOfStock = 0;
		for(StoreProduct sp:storeProductList) {
			sumOfStock += sp.getStoreProductStock();
		}
		if(sumOfStock < requestQuantity) return "error:out of stock";
	
			//유통기한 남은 재고 있음 + 재고가 사용자의 요구 개수 충족
			Store store = storeProductList.get(0).getStore();
			
		//2 CartProduct에 프로덕트 및 추가
		//요구 횟수 채울 떄까지 반복
		for(StoreProduct sp:storeProductList) {
			if(requestQuantity <= 0) break;
			Product productNow = sp.getProduct();
			int productNowQuantity = sp.getStoreProductStock();
			
			//이미 장바구니에 같은 이름의 행 존재하나 확인
			List<CartProduct> cartProductList = cartProductRepository.findByProduct_ProductTitle(productName);
			CartProduct cartProduct;
			
			//없을시 새로 생성
			if(cartProductList == null || cartProductList.size() == 0) {
				cartProduct = new CartProduct();
			}else {
				//있을시 기존 엔티티 사용
				cartProduct = cartProductList.get(0);
			}
			int cartExistingQuantity = cartProduct.getCartProductQuantity();
			

			
			cartProduct.setCart(cart);
			cartProduct.setProduct(sp.getProduct());
			
			//수량 초과시 최대 개수로 채우고 얼리 리턴
			if(sumOfStock < cartExistingQuantity + productNowQuantity) {
				cartProduct.setCartProductQuantity(sumOfStock);
				cartProductRepository.save(cartProduct);
				
				return "error:full of bound";
			}
			
			if (requestQuantity - productNowQuantity < 0) {//요구량 < 수량
				cartProduct.setCartProductQuantity(cartExistingQuantity + requestQuantity);
				System.out.println("발생1");
			    requestQuantity = 0;
			} else  {// 요구량 > 수량
				cartProduct.setCartProductQuantity(cartExistingQuantity + productNowQuantity);
				System.out.println("발생2");

			    requestQuantity -= productNowQuantity;
			}
				
			//실제 주문에 들어가면더 나을 것 같은 로직
//			//현재 프로덕트에서 주문 개수 채울수 있으면 전체 다 넣기 
//			if (requestQuantity - productNowQuantity < 0) {
//				cartProduct.setCartProductQuantity(cartExistingQuantity + requestQuantity);
//			    requestQuantity = 0;
//			} else {//부족하면 프로덕트 주문개수만 넣기
//				cartProduct.setCartProductQuantity(productNowQuantity);
//			    requestQuantity -= productNowQuantity;
//			}
//			
			cartProductRepository.save(cartProduct);
		}
		return "success";
	}


	
}
