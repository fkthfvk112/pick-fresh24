package mart.fresh.com.controller;

import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.repository.OrderedProductRepository;
import mart.fresh.com.service.OrderedProductService;
import reactor.core.publisher.Flux;

@RequestMapping("/orderedproduct")
@RestController
public class OrderedProductController {
private final OrderedProductService orderedProductService;



	@Autowired
	public OrderedProductController(OrderedProductService orderedProductService,
														OrderedProductRepository orderedProductRepository) {
		this.orderedProductService = orderedProductService;

	}
	
	@PostMapping("/orderedproduct-list")
	public Page<MyOrderedProductDto> getOrderedProductBymemberId(	Authentication authentication,
																	@RequestParam int page, @RequestParam int size) {
		System.out.println("OrderedProductController getOrderedProductByorderedProductId : " );

		Page<MyOrderedProductDto> orderedProductList = orderedProductService.getOrderedProductByMemberId(authentication.getName(), page-1, size);
		System.out.println("orderedProductList orderedProductList orderedProductList" + orderedProductList.toString());
		return orderedProductList;
	}
	
	@CrossOrigin(origins = "http://localhost:3000")
	@GetMapping(value = "/storeordered-list", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//	public Flux<MyOrderedProductDto> getOrderedlistByStoreId(	Authentication authentication) {
	public Flux<MyOrderedProductDto> getOrderedlistByStoreId(String memberId) {
		
//		System.out.println("OrderedProductController getOrderedProductByorderedProductId :  " + authentication.getName());
		System.out.println("OrderedProductController getOrderedProductByorderedProductId :  " + memberId);
		
		
//		   return orderedProductService.streamOrderedProductsByStoreId(authentication.getName());
		   return orderedProductService.streamOrderedProductsByStoreId(memberId);
	}


	
	@PostMapping("/create-order")
//	public void createOrder(@RequestBody OrderedProduct orderedProduct) {
	public void createOrder(@RequestBody MyOrderedProductDto myOrderedProductDto) {
		System.out.println("OrderedProductController OrderedProductController 값 확인 : " + myOrderedProductDto.toString());
		
		orderedProductService.createOrder(myOrderedProductDto);
	}
}