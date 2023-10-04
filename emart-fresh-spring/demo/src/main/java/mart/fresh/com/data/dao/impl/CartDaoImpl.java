package mart.fresh.com.data.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jakarta.persistence.Transient;
import jakarta.transaction.Transactional;
import mart.fresh.com.data.dao.CartDao;
import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.dto.CartProductDto;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.entity.StoreProduct;
import mart.fresh.com.data.entity.StoreProductWithId;
import mart.fresh.com.data.repository.CartProductRepository;
import mart.fresh.com.data.repository.CartRepository;
import mart.fresh.com.data.repository.MemberRepository;
import mart.fresh.com.data.repository.StoreProductObjRepository;
import mart.fresh.com.data.repository.StoreProductRepository;
import mart.fresh.com.data.repository.StoreRepository;

@Component
public class CartDaoImpl implements CartDao {
    private final CartRepository cartRepository;
    private final MemberRepository memberRepository;
    private final StoreRepository storeRepository;
    private final CartProductRepository cartProductRepository;
    private final StoreProductObjRepository storeProductObjRepository;
    
    @Autowired
    public CartDaoImpl(CartRepository cartRepository, MemberRepository memberRepository,
    		StoreRepository storeRepository, CartProductRepository cartProductRepository,
    		StoreProductObjRepository storeProductObjRepository) {
        this.cartRepository = cartRepository;
        this.memberRepository = memberRepository;
        this.storeRepository = storeRepository;
        this.cartProductRepository = cartProductRepository;
        this.storeProductObjRepository = storeProductObjRepository;
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
						
			cartProductRepository.save(cartProduct);
		}
		return "success";
	}

	
	
	//수정 : 동시성 문제 세마포어로 관리
	@Transactional
	@Override
	public String decreaseCartProductQuantity(String memberId) {
		System.out.println("멤버 아이디" + memberId);
		Cart myCart =  cartRepository.findByMember_MemberId(memberId);
		//카트 아이디
		int cartId = myCart.getCartId();
		int storeId = myCart.getStore().getStoreId();
		
		//내 주문 리스트
		List<CartProduct> cartProductList =  cartProductRepository.findCartProductListByCartId(cartId);
		
		//가게 재고
		List<StoreProduct> spList = storeProductObjRepository.findtStoreProuctByStoreId(storeId);
		
		for(CartProduct orderCp:cartProductList) {
			String orederdProductName = orderCp.getProduct().getProductTitle();
			int orederedProductQuantity = orderCp.getCartProductQuantity();
			
			for(StoreProduct storeProduct:spList) {
				String storeProductname = storeProduct.getProduct().getProductTitle();
				int storeProductQuantity = storeProduct.getStoreProductStock();//퀀티티

				if(storeProductname.equals(orederdProductName)) {//현재 검사하는 가게 물품과 내가 주문한 물품 일치
					if(orederedProductQuantity > storeProductQuantity) {//요구 수량이 가게 수량보다 많을 경우
						storeProductQuantity = 0;
						storeProduct.setStoreProductStock(storeProductQuantity);
						storeProductObjRepository.save(storeProduct);
						
						orederedProductQuantity -= storeProductQuantity;
					} 
					else if(orederedProductQuantity <= storeProductQuantity){//요구 수량이 가게 수량보다 적을 경우
						storeProductQuantity -= orederedProductQuantity;
						orederedProductQuantity = 0;
						
						storeProduct.setStoreProductStock(storeProductQuantity);
						storeProductObjRepository.save(storeProduct);
						cartProductRepository.delete(orderCp);
						break;//다음 내 품목으로
					}
				}
			}
			
			if(orederedProductQuantity >= 1) throw new RuntimeException("수량 처리가 불가합니다.");
		}
		return "success";
	}

	@Override
	public void saveCart(Cart cart) {
		cartRepository.save(cart);
	}
	
}
