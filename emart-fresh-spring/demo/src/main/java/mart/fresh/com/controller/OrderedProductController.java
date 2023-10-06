package mart.fresh.com.controller;


import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.service.OrderedProductService;
import reactor.core.publisher.Flux;
import mart.fresh.com.data.dto.OrderedInfoDto;
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
import mart.fresh.com.service.ProductService;
import mart.fresh.com.service.ReviewService;
import mart.fresh.com.service.StoreService;


@RequestMapping("/orderedproduct")
@RestController
public class OrderedProductController {
private final OrderedProductService orderedProductService;
private final OrderedProductProductService orderedProductProductService;
private final MemberService memberService;
private final StoreService storeService;
private final ReviewService reviewService;
private final CouponService couponService;
private final ProductService productService;

	@Autowired
	public OrderedProductController(OrderedProductService orderedProductService, OrderedProductProductService orderedProductProductService, 
			MemberService memberService, StoreService storeService, CouponService couponService, ProductService productService, ReviewService reviewService) {
		this.orderedProductService = orderedProductService;
		this.orderedProductProductService = orderedProductProductService;
		this.memberService = memberService;
		this.storeService = storeService;
		this.couponService = couponService;
		this.productService = productService;
		this.reviewService = reviewService;

	}
	
	@GetMapping("/orderedproduct-list")
	public Page<MyOrderedProductDto> getOrderedProductBymemberId(	Authentication authentication,
																	@RequestParam int page, @RequestParam int size) {
		
		System.out.println("OrderedProductController getOrderedProductByorderedProductId : " );
		
		Page<MyOrderedProductDto> orderedProductList = orderedProductService.getOrderedProductByMemberId(authentication.getName(), page-1, size);
		System.out.println("orderedProductList orderedProductList orderedProductList" + orderedProductList.toString());
		return orderedProductList;
	}
	
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(value = "/storeordered-list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<MyOrderedProductDto> getOrderedlistByStoreId(@RequestParam String memberId) {

		System.out.println("OrderedProductController getOrderedProductByorderedProductId :  " + memberId);
	
		   return orderedProductService.streamOrderedProductsByStoreId(memberId);


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
	
	@GetMapping("/getOrderedInfo")
	public ResponseEntity<List<OrderedInfoDto>> getOrderedInfo(Authentication authentication) {
	    try {
	        String memberId = authentication.getName();
	        System.out.println("OrderedProductController " + memberId + "의 주문내역 " + new Date());

	        List<OrderedProduct> orderedProductList = orderedProductService.findByMemberMemberId(memberId);
	        List<OrderedInfoDto> orderedInfoDtoList = new ArrayList<>();

	        for (OrderedProduct orderedProduct : orderedProductList) {
	            List<OrderedProductProduct> orderedProductProductList =
	                    orderedProductProductService.findByOrderedProductOrderedProductId(orderedProduct.getOrderedProductId());

	            OrderedInfoDto orderedInfoDto = new OrderedInfoDto();
	            orderedInfoDto.setOrderedProductId(orderedProduct.getOrderedProductId());
	            orderedInfoDto.setStoreId(orderedProduct.getStore().getStoreId());
	            orderedInfoDto.setStoreName(orderedProduct.getStore().getStoreName());
	            if (orderedProduct.getCoupon() != null) {
	                orderedInfoDto.setCouponId(orderedProduct.getCoupon().getCouponId());
	            } else {
	                orderedInfoDto.setCouponId(0);
	            }	            
	            orderedInfoDto.setTotalAmount(orderedProduct.getTotalAmount());
	            orderedInfoDto.setPickup(orderedProduct.isPickup());
	            orderedInfoDto.setOrderedDel(orderedProduct.isOrderedDel());
	            orderedInfoDto.setOrderedDate(orderedProduct.getOrderedDate());
	            
	            List<OrderedProductProductDto> orderedProductProductDtoList = new ArrayList<>();
	            
	            for (OrderedProductProduct orderedProductProduct : orderedProductProductList) {
	                OrderedProductProductDto orderedProductProductDto = new OrderedProductProductDto();
	                orderedProductProductDto.setProductId(orderedProductProduct.getProduct().getProductId());
	                orderedProductProductDto.setOrderedQuantity(orderedProductProduct.getOrderedQuantity());
	                orderedProductProductDto.setOrderedProductId(orderedProductProduct.getOrderedProductProductId());
	                Product product = orderedProductProductService.getProductDetails(orderedProductProduct.getProduct().getProductId());
	                
	                orderedProductProductDto.setProductTitle(product.getProductTitle());
	                orderedProductProductDto.setPrice(product.getPriceNumber());
	                orderedProductProductDtoList.add(orderedProductProductDto);
	            }

	            orderedInfoDto.setOrderedProductProduct(orderedProductProductDtoList);
	            orderedInfoDtoList.add(orderedInfoDto);
	        }

	        System.out.println("주문 내역 출력 : " + orderedInfoDtoList);
	        return ResponseEntity.ok(orderedInfoDtoList);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }

	}	
	
	@PostMapping("/completepickup")
	public ResponseEntity<String> completePickup(@RequestBody Map<String, Integer> orderProductId) {
	    int orderedProductId = orderProductId.get("orderedProductId");

		System.out.println("여기" + orderedProductId);
		try {
			orderedProductService.completePickup(orderedProductId);
		System.out.println("픽업처리 완료");
		return ResponseEntity.ok("픽업처리 완료");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("픽업처리 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예외 에러 : " + e.getMessage());
		}
	}

	
	@GetMapping("/getProductDetails")
	public ResponseEntity<List<Map<String, Object>>> getProductDetails(@RequestParam("orderedProductId") int orderedProductId) {
		System.out.println("OrderedProductController 주문내역보기 " + new Date() + "아이디" + orderedProductId);
		
	    try {
	    	OrderedProduct orderedProduct = orderedProductService.findByOrderedProductId(orderedProductId);

	    	String memberId = orderedProduct.getMember().getMemberId();
	    	
	    	System.out.println("================== : " + memberId);
	    	List<OrderedProductProduct> orderedProductProductList = orderedProductProductService.findByOrderedProductOrderedProductId(orderedProductId);

	        if (orderedProductProductList.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
	        }

	        List<Map<String, Object>> productDetailsList = new ArrayList<>();

	        for (OrderedProductProduct orderedProductProduct : orderedProductProductList) {
	            Map<String, Object> productDetails = new HashMap<>();
	            int productId = orderedProductProduct.getProduct().getProductId();
	            Product product = productService.findByProductId(productId);
	            productDetails.put("productName", product.getProductTitle());
	            productDetails.put("productImgUrl", product.getProductImgUrl());
	            productDetails.put("price", product.getPriceNumber());
	            productDetails.put("orderedQuantity", orderedProductProduct.getOrderedQuantity());
	            productDetails.put("review", orderedProductProduct.getReview());
	            productDetailsList.add(productDetails);
	        }

	        return ResponseEntity.ok(productDetailsList);
	    } catch (Exception e) {
	        e.printStackTrace();
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
	}

}