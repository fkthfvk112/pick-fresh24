package mart.fresh.com.controller;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.dto.OrderedProductInfoDto;
import mart.fresh.com.data.dto.OrderedProductProductDto;
import mart.fresh.com.data.entity.Coupon;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.service.CouponService;
import mart.fresh.com.service.MemberService;
import mart.fresh.com.service.OrderedProductProductService;
import mart.fresh.com.service.OrderedProductService;
import mart.fresh.com.service.ProductService;
import mart.fresh.com.service.StoreService;

@RequestMapping("/orderedproduct")
@RestController
public class OrderedProductController {

private final OrderedProductService orderedProductService;
private final OrderedProductProductService orderedProductProductService;
private final MemberService memberService;
private final StoreService storeService;
private final CouponService couponService;
private final ProductService productService;

	@Autowired
	public OrderedProductController(OrderedProductService orderedProductService, OrderedProductProductService orderedProductProductService, 
			MemberService memberService, StoreService storeService, CouponService couponService, ProductService productService) {
		this.orderedProductService = orderedProductService;
		this.orderedProductProductService = orderedProductProductService;
		this.memberService = memberService;
		this.storeService = storeService;
		this.couponService = couponService;
		this.productService = productService;
	}
	
	@GetMapping("/orderedproduct-list")
	public Page<MyOrderedProductDto> getOrderedProductBymemberId(	Authentication authentication,
																	@RequestParam int page, @RequestParam int size) {
		System.out.println("OrderedProductController getOrderedProductByorderedProductId : " );

		Page<MyOrderedProductDto> orderedProductList = orderedProductService.getOrderedProductByMemberId(authentication.getName(), page-1, size);
		System.out.println("orderedProductList orderedProductList orderedProductList" + orderedProductList.toString());
		return orderedProductList;
	}
	
	@PostMapping("/saveOrderedProductInfo")
	public ResponseEntity<String> saveOrderedProductInfo(Authentication authentication,
			@RequestBody OrderedProductInfoDto orderedProductInfoDto) {
		System.out.println("OrderedProductController 주문 내역 저장 " + new Date());
		try {
			System.out.println("프론트에서 준 주문 내역 : " + orderedProductInfoDto);

			String memberId = authentication.getName();
			Member member = memberService.findByMemberId(memberId);

			Store store = storeService.findByStoreId(orderedProductInfoDto.getStoreId());
			Coupon coupon = couponService.findByCouponId(orderedProductInfoDto.getCouponId());

			OrderedProduct orderedProduct = new OrderedProduct();
			orderedProduct.setMember(member);
			orderedProduct.setStore(store);
			orderedProduct.setCoupon(coupon);
			orderedProduct.setOrderedProductId(orderedProductInfoDto.getOrderedProductId());
			Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
		    orderedProduct.setOrderedDate(currentTimestamp);
			orderedProduct.setOrderedDel(false);
			orderedProduct.setPickup(false);
			orderedProduct.setTotalAmount(orderedProductInfoDto.getTotalAmount());
			orderedProductService.saveOrderedProduct(orderedProduct);
			
	        for (OrderedProductProductDto productDto : orderedProductInfoDto.getOrderedProductProduct()) {
	            Product product = productService.findByProductId(productDto.getProductId());
	            OrderedProductProduct orderedProductProduct = new OrderedProductProduct();
	            orderedProductProduct.setOrderedProduct(orderedProduct);
	            orderedProductProduct.setProduct(product);
	            orderedProductProduct.setOrderedQuantity(productDto.getOrderedQuantity());
	            orderedProductProductService.saveOrderedProductProduct(orderedProductProduct);
	        }

			System.out.println("주문 내역 저장 성공");
			return ResponseEntity.ok("주문 내역 저장 성공");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("주문 내역 저장 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예외 에러 : " + e.getMessage());
		}
	}
	
	
	
// 구현 예정
//	@PostMapping("/storeordered-list")
//	public Page<MyOrderedProductDto> getOrderedlistByStoreId(	@RequestParam String memberId, @RequestParam int page, @RequestParam int size,
//																HttpSession session) {
//		System.out.println("OrderedProductController getOrderedProductByorderedProductId : " );
//		
//		Store storeInfo = orderedProductRepository.findByMemberId(memberId);
//		
//		Page<MyOrderedProductDto> orderedProductList = orderedProductService.getOrderedListByStoreId(storeInfo.getStoreId(), page-1, size);
//
//		session.setAttribute("storeId", storeInfo.getStoreId());
//		
//		return orderedProductList;
//		
//
//	}
	
	
}