package mart.fresh.com.controller;

import java.util.ArrayList;
import java.util.Date;
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
import mart.fresh.com.service.CartProductService;
import mart.fresh.com.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {
	private final CartService cartService;
    private final CartProductService cartProductService;
	
	@Autowired
	public CartController(CartService cartService, CartProductService cartProductService) {
		this.cartService = cartService;
        this.cartProductService = cartProductService;
	}
	
	@GetMapping("/getCartInfo")
    public ResponseEntity<List<CartInfoDto>> getCartInfoByMember(Authentication authentication) {
        String memberId = authentication.getName();
        System.out.println("CartController "+ memberId+ "의 장바구니 확인 " + new Date());
		List<CartInfoDto> cartInfoList = cartService.getCartInfo(memberId);
        if (cartInfoList.isEmpty()) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.ok(cartInfoList);
        }
    }
	
	@PostMapping("/updateCartProductQuantity")
	public ResponseEntity<?> updateCartProductQuantity(Authentication authentication, @RequestBody List<Map<String, Integer>> requestItems) {
	    String memberId = authentication.getName();
	    System.out.println("CartController " + memberId + "의 장바구니 물품 수량 변경 " + new Date());

	    try {
	        List<CartInfoDto> updatedCartInfoList = new ArrayList<>();

	        for (Map<String, Integer> item : requestItems) {
	            int cartProductId = item.get("cartProductId");
	            int cartProductQuantity = item.get("cartProductQuantity");

	            boolean success = cartProductService.updateCartProductQuantity(memberId, cartProductId, cartProductQuantity);

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
	
	@PostMapping("/addToCart")//수정 : add auth
	public String addToCart(Authentication authentication, @RequestBody AddToCartDto dto) {

	    System.out.println("CartController의 장바구니에 추가 " + new Date());
	    System.out.println("CartController" + dto.getProductName());
	    System.out.println("CartController" + dto.getRequestQuantity());
	    System.out.println("CartController" + authentication.getName());

	    return cartService.addToCart(authentication.getName(), dto.getProductName(), dto.getStoreId(), dto.getRequestQuantity());//수정 memid
	}
	
	
	@DeleteMapping("/removeProduct")
    public ResponseEntity<String> removeProductFromCart(Authentication authentication, @RequestParam int cartProductId) {
		String memberId = authentication.getName();
        System.out.println("CartController "+ memberId+ "의 장바구니 삭제 " + new Date());

        cartProductService.removeProductFromCart(memberId, cartProductId);
        return ResponseEntity.status(HttpStatus.OK).build();

    }
	
	@PostMapping("/decreaseCartProduct")
	public String decreaseCartProductQuantity(Authentication authentication){
		System.out.println("이거요");
		//String memberId = authentication.getName();
		return cartService.decreaseCartProductQuantity(authentication.getName());
	}

	@GetMapping("/myCartStoreId")
	public int getMyCartStoreId(String memberId) {
		System.out.println("들어옴 ");

		System.out.println("반환값 " + cartService.getMyCartStoreId(memberId));
		return cartService.getMyCartStoreId(memberId);
	}
	
}
