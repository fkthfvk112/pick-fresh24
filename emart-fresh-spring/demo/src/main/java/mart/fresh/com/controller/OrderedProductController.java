package mart.fresh.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.servlet.http.HttpSession;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.OrderedProductRepository;
import mart.fresh.com.service.OrderedProductService;

@RequestMapping("/orderedproduct")
@RestController
public class OrderedProductController {

private final OrderedProductService orderedProductService;
private final OrderedProductRepository orderedProductRepository;

	@Autowired
	public OrderedProductController(OrderedProductService orderedProductService, OrderedProductRepository orderedProductRepository) {
		this.orderedProductService = orderedProductService;
		this.orderedProductRepository = orderedProductRepository;
	}
	
	@GetMapping("/orderedproduct-list")
	public Page<MyOrderedProductDto> getOrderedProductBymemberId(	Authentication authentication,
																	@RequestParam int page, @RequestParam int size) {
		System.out.println("OrderedProductController getOrderedProductByorderedProductId : " );

		Page<MyOrderedProductDto> orderedProductList = orderedProductService.getOrderedProductByMemberId(authentication.getName(), page-1, size);
		System.out.println("orderedProductList orderedProductList orderedProductList" + orderedProductList.toString());
		return orderedProductList;
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