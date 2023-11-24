package mart.fresh.com.data.dao.impl;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashMap;
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
import mart.fresh.com.data.dto.ProductInfoDto;
import mart.fresh.com.data.dto.ProductProcessResult;
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

	// 한 번에 총 수량 넘는 만큼 추가하면 error
	// 하지만 장바구니에 나누어서 담아서 수량 추가하는건 ok하도록 로직 구현함
	@Override
	public String addToCart(String memberId, String productName, int storeId, int requestQuantity) {

		// 내 카트 받아오기
		List<Cart> cartList = cartRepository.findByMemberMemberId(memberId);

		Cart cart = cartList.get(0);
		
		if (cart == null)
			return "장바구니 테이블 생성 안 되어있음";
		
		if (cart.getStore() == null) {
	        Store store = Store.builder().build();
	        store.setStoreId(storeId);
	        cart.setStore(store);
	        cartRepository.save(cart);
	    }
		

		// 다른 가게가 카트에 이미 존재하면 에러 :
		// 0은 빈 상태, 즉 에러 X		
		if (cart.getStore().getStoreId() != 0 && cart.getStore().getStoreId() != storeId) {
	        return "error:another store exist";
	    }

		System.out.println("-------------adst : ");
		Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		List<StoreProduct> storeProductList = cartRepository.getAvailableProducts(storeId, productName,
				currentTimestamp);
		System.out.println("-------------addToCart productList : ");

		// 해당 상품의 총 개수를 구함
		if (storeProductList == null || storeProductList.size() <= 0)
			return "error:out of stock";
		int sumOfStock = 0;
		for (StoreProduct sp : storeProductList) {
			sumOfStock += sp.getStoreProductStock();
		}
		if (sumOfStock < requestQuantity)
			return "error:out of stock";

		// 유통기한 남은 재고 있음 + 재고가 사용자의 요구 개수 충족
		Store store = storeProductList.get(0).getStore();

		// 2 CartProduct에 프로덕트 및 추가
		// 요구 횟수 채울 떄까지 반복
		for (StoreProduct sp : storeProductList) {
			if (requestQuantity <= 0)
				break;
			Product productNow = sp.getProduct();
			int productNowQuantity = sp.getStoreProductStock();

			// 이미 장바구니에 같은 이름의 행 존재하나 확인
			List<CartProduct> cartProductList = cartProductRepository.findByProduct_ProductTitle(productName);
			CartProduct cartProduct;

			// 없을시 새로 생성
			if (cartProductList == null || cartProductList.size() == 0) {
				cartProduct = new CartProduct();
			} else {
				// 있을시 기존 엔티티 사용
				cartProduct = cartProductList.get(0);
			}
			int cartExistingQuantity = cartProduct.getCartProductQuantity();

			cartProduct.setCart(cart);
			cartProduct.setProduct(sp.getProduct());

			// 수량 초과시 최대 개수로 채우고 얼리 리턴
			if (sumOfStock < cartExistingQuantity + requestQuantity) {
				System.out.println("스톡 벨류" + sumOfStock);
				System.out.println("존재" + cartExistingQuantity);
				System.out.println("리퀘" + requestQuantity);

				cartProduct.setCartProductQuantity(sumOfStock);
				cartProductRepository.save(cartProduct);

				return "error:full of bound";
			}

			if (requestQuantity - productNowQuantity < 0) {// 요구량 < 수량
				cartProduct.setCartProductQuantity(cartExistingQuantity + requestQuantity);
				System.out.println("발생1");
				requestQuantity = 0;
			} else {// 요구량 > 수량
				cartProduct.setCartProductQuantity(cartExistingQuantity + productNowQuantity);
				System.out.println("발생2");

				requestQuantity -= productNowQuantity;
			}

			cartProductRepository.save(cartProduct);
		}
		return "success";
	}

	@Override
	public void saveCart(Cart cart) {
		cartRepository.save(cart);
	}

	public int getMyCartStoreId(String memberId) {
		List<Cart> cartList = cartRepository.findByMemberMemberId(memberId);
		if (cartList == null)
			return -1;

		Cart cart = cartList.get(0);

		return cart.getStore().getStoreId();
	}

	@Override
	public Cart findByMember(Member member) {
		return cartRepository.findByMember(member);
	}

	@Override
	public String recoverStoreProductQuantity(String memberId, ProductProcessResult productProcessResult) {
		Cart cart = cartRepository.findByMember_MemberId(memberId);
		Store store = cart.getStore();

		Map<Integer, Integer> productInfoMap = productProcessResult.getProductInfoMap();

		Map<Integer, Integer> successfulRecoveries = new HashMap<>();

		for (Map.Entry<Integer, Integer> entry : productInfoMap.entrySet()) {
			Integer productId = entry.getKey();
			Integer recoveredQuantity = entry.getValue();

			StoreProduct storeProduct = storeProductObjRepository
					.findByStoreStoreIdAndProductProductId(store.getStoreId(), productId);

			if (storeProduct == null) {
				System.out.println("productId " + productId + "인 상품을 찾지 못함.");

				for (Map.Entry<Integer, Integer> successfulEntry : successfulRecoveries.entrySet()) {
					Integer successfulProductId = successfulEntry.getKey();
					Integer successfulRecoveredQuantity = successfulEntry.getValue();

					StoreProduct successfulStoreProduct = storeProductObjRepository
							.findByStoreStoreIdAndProductProductId(store.getStoreId(), successfulProductId);

					successfulStoreProduct.setStoreProductStock(
							successfulStoreProduct.getStoreProductStock() - successfulRecoveredQuantity);
					storeProductObjRepository.save(successfulStoreProduct);
				}
				return "notFoundProductId";
			}

			successfulRecoveries.put(productId, recoveredQuantity);

			storeProduct.setStoreProductStock(storeProduct.getStoreProductStock() + recoveredQuantity);
			storeProductObjRepository.save(storeProduct);
		}

		return "success";
	}

	@Transactional
	@Override
	public ProductProcessResult decreaseStoretProductQuantity(String memberId, List<ProductInfoDto> productInfoList) {
		Cart cart = cartRepository.findByMember_MemberId(memberId);
		Store store = cart.getStore();
		List<CartProduct> cartProductList = cartProductRepository.findCartProductListByCartId(cart.getCartId());
		Map<Integer, Integer> productInfoMap = new HashMap<>();

		for (ProductInfoDto productInfo : productInfoList) {
			String productTitle = productInfo.getProductTitle();
			int productQuantity = productInfo.getProductQuantity();

			boolean productExistsInCart = cartProductList.stream()
					.anyMatch(cartProduct -> cartProduct.getProduct().getProductTitle().equals(productTitle));

			if (!productExistsInCart) {
				System.out.println(productTitle + "이[가] " + memberId + "의 장바구니에 없습니다");
				throw new RuntimeException("notFoundCartProduct");
			}

			for (CartProduct cartProduct : cartProductList) {
				if (cartProduct.getProduct().getProductTitle().equals(productTitle)) {
					if (productQuantity > cartProduct.getCartProductQuantity()) {
						System.out.println("요청한 수량이 장바구니에 담긴 상품의 수량보다 많습니다. 해당 상품 : " + productTitle);
						throw new RuntimeException("tooMuchQuantity");
					}
					break;
				}
			}

			List<StoreProduct> storeProductList = storeProductObjRepository
					.findtStoreProuctByStoreId(store.getStoreId());
			int totalStock = getTotalStockForProduct(storeProductList, productTitle);
			System.out.println(productTitle + "의 가게 전체 수량 : " + totalStock);

			if (productQuantity > totalStock) {
				System.out.println("요청한 수량이 가게에 총 재고보다 많습니다.");
				throw new RuntimeException("lackOfStock");
			}

			int remainingQuantity = productQuantity;
			for (StoreProduct storeProduct : storeProductList) {
				if (storeProduct.getProduct().getProductTitle().equals(productTitle)) {
					int productId = storeProduct.getProduct().getProductId();
					int currentStock = storeProduct.getStoreProductStock();
					if (remainingQuantity <= 0) {
						System.out.println("재고 감소 끝 !");
						break;
					}

					if (currentStock >= remainingQuantity) {
						storeProduct.setStoreProductStock(currentStock - remainingQuantity);
						storeProductObjRepository.save(storeProduct);

						System.out.println("productId[" + productId + "]인 " + productTitle + " 재고를 " + remainingQuantity
								+ "만큼 감소시켰습니다.");

						productInfoMap.put(productId, remainingQuantity);
						remainingQuantity = 0;
					} else {
						remainingQuantity -= currentStock;
						storeProduct.setStoreProductStock(0);
						storeProductObjRepository.save(storeProduct);

						productInfoMap.put(productId, currentStock);
						System.out.println("productId[" + productId + "]인 " + productTitle + " 재고가 부족하여 모두 소진되었습니다.");
					}
				}
			}

		}
		System.out.println("제품 정보: " + productInfoMap);
		return new ProductProcessResult("success", productInfoMap);
	}
	
	@Transactional
	@Override
	public ProductProcessResult decreaseCartProductQuantity(String memberId, List<ProductInfoDto> productInfoList) {
		Cart cart = cartRepository.findByMember_MemberId(memberId);
		List<CartProduct> cartProductList = cartProductRepository.findCartProductListByCartId(cart.getCartId());
		Map<Integer, Integer> productInfoMap = new HashMap<>();

		for (ProductInfoDto productInfo : productInfoList) {
			String productTitle = productInfo.getProductTitle();
			int productQuantity = productInfo.getProductQuantity();

			boolean productExistsInCart = cartProductList.stream()
					.anyMatch(cartProduct -> cartProduct.getProduct().getProductTitle().equals(productTitle));

			if (!productExistsInCart) {
				System.out.println(productTitle + "이[가] " + memberId + "의 장바구니에 없습니다");
				throw new RuntimeException("notFoundCartProduct");
			}

			for (CartProduct cartProduct : cartProductList) {
				if (cartProduct.getProduct().getProductTitle().equals(productTitle)) {
					if (productQuantity > cartProduct.getCartProductQuantity()) {
						System.out.println("요청한 수량이 장바구니에 담긴 상품의 수량보다 많습니다. 해당 상품 : " + productTitle);
						throw new RuntimeException("tooMuchQuantity");
					}
					break;
				}
			}
			
			for (CartProduct cartProduct : cartProductList) {
				int productId = cartProduct.getProduct().getProductId();
				if (cartProduct.getProduct().getProductTitle().equals(productTitle)) {
					cartProduct.setCartProductQuantity(
							cartProduct.getCartProductQuantity() - productQuantity);
					cartProductRepository.save(cartProduct);
	                productInfoMap.put(productId, cartProduct.getCartProductQuantity());
					break;
				}
			}
			
		}
		System.out.println("제품 정보: " + productInfoMap);
		return new ProductProcessResult("success", productInfoMap);
	}
	
	
	private int getTotalStockForProduct(List<StoreProduct> storeProductList, String productTitle) {
		int totalStock = 0;
		for (StoreProduct storeProduct : storeProductList) {
			if (storeProduct.getProduct().getProductTitle().equals(productTitle)) {
				totalStock += storeProduct.getStoreProductStock();
			}
		}
		return totalStock;
	}

}
