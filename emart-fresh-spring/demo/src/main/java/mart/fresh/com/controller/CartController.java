package mart.fresh.com.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import mart.fresh.com.data.dto.AddToCartDto;
import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.dto.ProductInfoDto;
import mart.fresh.com.data.dto.ProductProcessResult;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.service.CartProductService;
import mart.fresh.com.service.CartService;
import mart.fresh.com.service.MemberService;
import mart.fresh.com.service.StoreService;

@RestController
@RequestMapping("/cart")
public class CartController {
	private final CartService cartService;
	private final StoreService storeService;
	private final CartProductService cartProductService;
	private final MemberService memberService;

	@Autowired
	public CartController(CartService cartService, CartProductService cartProductService, MemberService memberService,
			StoreService storeService) {
		this.cartService = cartService;
		this.cartProductService = cartProductService;
		this.memberService = memberService;
		this.storeService = storeService;
	}

	@GetMapping("/getCartInfo")
	public ResponseEntity<List<CartInfoDto>> getCartInfoByMember(Authentication authentication) {
	    String memberId = authentication.getName();
	    System.out.println("CartController " + memberId + "의 장바구니 확인 " + new Date());
	    
	    List<CartInfoDto> cartInfoList = cartService.getCartInfo(memberId);
	    List<CartInfoDto> filteredCartInfoList = new ArrayList<>();
	    
	    System.out.println("getCartIngfo - cartInfoList" + cartInfoList);
	    for (CartInfoDto cartInfo : cartInfoList) {
	        if (cartInfo.getCartProductQuantity() > 0) {
	        	Store store = storeService.findByStoreId(cartInfo.getStoreId());
	        	String storeName = store.getStoreName();
	        	cartInfo.setStoreName(storeName);
	            filteredCartInfoList.add(cartInfo);
	        }
	    }
	    System.out.println("getCartIngfo - filteredCartInfoList" + filteredCartInfoList);

	    if (filteredCartInfoList.isEmpty()) {
		    System.out.println("no content");

	        return ResponseEntity.noContent().build();
	    } else {
	        return ResponseEntity.ok(filteredCartInfoList);
	    }
	}


	@PostMapping("/updateCartProductQuantity")
	public ResponseEntity<?> updateCartProductQuantity(Authentication authentication,
			@RequestBody List<Map<String, Integer>> requestItems) {
		String memberId = authentication.getName();
		System.out.println("CartController " + memberId + "의 장바구니 물품 수량 변경 " + new Date());

		try {
			List<CartInfoDto> updatedCartInfoList = new ArrayList<>();

			for (Map<String, Integer> item : requestItems) {
				int cartProductId = item.get("cartProductId");
				int cartProductQuantity = item.get("cartProductQuantity");

				boolean success = cartProductService.updateCartProductQuantity(memberId, cartProductId,
						cartProductQuantity);

				if (!success) {
					System.out.println("장바구니 물품 수량 변경 실패");
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장바구니 수량 변경 실패");
				}
			}
			updatedCartInfoList = cartService.getCartInfo(memberId);

			System.out.println("장바구니 물품 수량 변경 성공");
			return ResponseEntity.ok(updatedCartInfoList);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장바구니 수량 변경 실패: " + e.getMessage());
		}
	}

	@PostMapping("/addToCart") // 수정 : add auth
	public String addToCart(Authentication authentication, @RequestBody AddToCartDto dto) {

		System.out.println("CartController의 장바구니에 추가 " + new Date());
		System.out.println("CartController" + dto.getProductName());
		System.out.println("CartController" + dto.getRequestQuantity());
		System.out.println("CartController" + authentication.getName());

		return cartService.addToCart(authentication.getName(), dto.getProductName(), dto.getStoreId(),
				dto.getRequestQuantity());// 수정 memid
	}

	@PostMapping("/checkCart")
	public ResponseEntity<String> checkCart(Authentication authentication, @RequestBody AddToCartDto dto) {
		try {
			String memberId = authentication.getName();

			System.out.println("CartController " + memberId + "의 장바구니 확인 " + new Date());

			int storeId = dto.getStoreId();
			Member member = memberService.findByMemberId(memberId);

			Cart cart = cartService.findByMember(member);
			CartProduct cartProduct = cartProductService.findByCart(cart);
			if (cart == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for memberId: " + memberId);
			}

			if (cart.getStore() == null) {
				Store store = new Store();
				store.setStoreId(storeId);
				cart.setStore(store);
				cartService.saveCart(cart);
			}
			
			if (cartProduct == null) {
				System.out.println("카트가 비었다.");
				cart.setStore(null);
				cartService.saveCart(cart);
				System.out.println("카트가 비었으니까 바로 추가해줄께");
				return ResponseEntity.ok(cartService.addToCart(authentication.getName(), dto.getProductName(),
						dto.getStoreId(), dto.getRequestQuantity()));
			}

			int cartStoreId = cart.getStore().getStoreId();

			if (cartStoreId == storeId) {
				return ResponseEntity.ok(cartService.addToCart(authentication.getName(), dto.getProductName(),
						dto.getStoreId(), dto.getRequestQuantity()));
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("멤버의 장바구니에 담긴 storeId와 지금 담을려는 storeId가 다름");
			}
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error checking cart: " + e.getMessage());
		}
	}

	@PostMapping("/changeCart/yes")
	public ResponseEntity<String> clearCartProducts(Authentication authentication, @RequestBody AddToCartDto dto) {
		try {
			String memberId = authentication.getName();
			Member member = memberService.findByMemberId(memberId);
			System.out.println("CartController " + memberId + "의 장바구니 교체 " + new Date());
			Cart cart = cartService.findByMember(member);

			if (cart == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart not found for memberId: " + memberId);
			}
			cartProductService.removeAllProductsFromCart(cart.getCartId());
			System.out.println("삭제 완료");

			int storeId = dto.getStoreId();
			Store store = storeService.findByStoreId(storeId);
			cart.setStore(store);
			cartService.saveCart(cart);

			return ResponseEntity.ok(cartService.addToCart(authentication.getName(), dto.getProductName(), storeId,
					dto.getRequestQuantity()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("Error clearing cart products: " + e.getMessage());
		}
	}

	@DeleteMapping("/removeProduct")
	public ResponseEntity<String> removeProductFromCart(Authentication authentication,
	        @RequestParam int cartProductId) {
	    String memberId = authentication.getName();
	    System.out.println("CartController " + memberId + "의 장바구니 물품 삭제 " + new Date());

	    boolean removalSuccessful = cartProductService.removeProductFromCart(memberId, cartProductId);
	    if (removalSuccessful) {
	        return ResponseEntity.status(HttpStatus.OK).build();
	    } else {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("장바구니 물품 삭제 실패");
	    }
	}


	@PostMapping("/decreaseCartProduct")
	public String decreaseCartProductQuantity(Authentication authentication) {
		System.out.println("이거요");
		// String memberId = authentication.getName();
		return cartService.decreaseCartProductQuantity(authentication.getName());
	}

	@PostMapping("/decreaseStoreProduct")
	public ResponseEntity<ProductProcessResult> decreaseStoreProductQuantity(Authentication authentication,
	        @RequestBody List<ProductInfoDto> productInfoList) {
	    String memberId = authentication.getName();
	    System.out.println("CartController 결제버튼 누르면 가게 재고 깍기 " + new Date());

	    try {
	        ProductProcessResult productProcessResult = cartService.decreaseStoretProductQuantity(memberId, productInfoList);
	        return ResponseEntity.ok(new ProductProcessResult("가게 재고 깍기 성공", productProcessResult.getProductInfoMap()));
	    } catch (RuntimeException ex) {
	        String errorMessage = ex.getMessage();
	        Map<Integer, Integer> productInfoMap = new HashMap<>();

	        if (errorMessage.equals("notFoundCartProduct")) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ProductProcessResult("장바구니에 해당 상품이 없습니다.", productInfoMap));
	        } else if (errorMessage.equals("tooMuchQuantity")) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(new ProductProcessResult("요청 수량이 장바구니의 수량보다 많습니다.", productInfoMap));
	        } else if (errorMessage.equals("lackOfStock")) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(new ProductProcessResult("요청 수량이 가게 재고보다 많습니다.", productInfoMap));
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(new ProductProcessResult("예상치 못한 오류 발생", productInfoMap));
	        }
	    }
	}
	
	@PostMapping("/decreaseCartProduct1")
	public ResponseEntity<ProductProcessResult> decreaseCartProductQuantity1(Authentication authentication,
			@RequestBody List<ProductInfoDto> productInfoList) {
		String memberId = authentication.getName();
	    System.out.println("CartController 결제 완료 시 장바구니 재고 깍기 " + new Date());
	    try {
	        ProductProcessResult productProcessResult = cartService.decreaseCartProductQuantity(memberId, productInfoList);
	        return ResponseEntity.ok(new ProductProcessResult("장바구니 재고 깍기 성공", productProcessResult.getProductInfoMap()));
	    } catch (RuntimeException ex) {
	        String errorMessage = ex.getMessage();
	        Map<Integer, Integer> productInfoMap = new HashMap<>();

	        if (errorMessage.equals("notFoundCartProduct")) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND)
	                    .body(new ProductProcessResult("장바구니에 해당 상품이 없습니다.", productInfoMap));
	        } else if (errorMessage.equals("tooMuchQuantity")) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(new ProductProcessResult("요청 수량이 장바구니의 수량보다 많습니다.", productInfoMap));
	        } else {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                    .body(new ProductProcessResult("예상치 못한 오류 발생", productInfoMap));
	        }
	    }
	}

	@PostMapping("/recoverStoreProduct")
	public ResponseEntity<String> recoverStoreProductQuantity(Authentication authentication, @RequestBody ProductProcessResult productProcessResult) {
	    String memberId = authentication.getName();
	    System.out.println("CartController 결제 취소로 가게 재고 회복 " + new Date());

	    String result = cartService.recoverStoreProductQuantity(memberId, productProcessResult);

	    if (result.equals("success")) {
	        return ResponseEntity.ok("가게 재고 회복 성공");
	    } else {
	        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
	    }
	}


	@GetMapping("/myCartStoreId")
	public int getMyCartStoreId(Authentication authentication) {
		String memberId = authentication.getName();
		System.out.println("들어옴 ");

		System.out.println("반환값 123" + cartService.getMyCartStoreId(memberId));
		return cartService.getMyCartStoreId(memberId);
	}

	@GetMapping("/myCartInfoCount")
	public ResponseEntity<Integer> getCartItemCountByProductTitle(Authentication authentication) {
		String memberId = authentication.getName();
		System.out.println("CartController " + memberId + "의 장바구니 물품 개수 " + new Date());

		try {
			List<CartInfoDto> cartInfoList = cartService.getCartInfo(memberId);
			Map<String, Integer> itemCountMap = new HashMap<>();

			for (CartInfoDto cartInfo : cartInfoList) {
				if (cartInfo.getCartProductQuantity() > 0) {
	                String productTitle = cartInfo.getProductTitle();
	                itemCountMap.put(productTitle, itemCountMap.getOrDefault(productTitle, 0) + 1);
	            }
			}

			return ResponseEntity.ok(itemCountMap.size());
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

}
