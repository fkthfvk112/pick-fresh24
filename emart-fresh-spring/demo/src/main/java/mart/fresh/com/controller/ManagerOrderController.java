package mart.fresh.com.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.ManagerOrderDto;
import mart.fresh.com.data.entity.ManagerOrderWithObj;
import mart.fresh.com.service.ManagerOrderService;
import mart.fresh.com.service.MemberService;
import mart.fresh.com.service.StoreService;


@RequestMapping("/order")
@RestController
public class ManagerOrderController {
	private final ManagerOrderService managerOrderService;
	private final StoreService storeService;
	
	@Autowired
	public ManagerOrderController(ManagerOrderService managerOrderService, StoreService storeService) {
		this.managerOrderService = managerOrderService;
		this.storeService = storeService;
	}
	
	@PostMapping("/add-manager-order")
	public String saveManagerOrder(Authentication authentication, @RequestBody List<ManagerOrderDto> managerOrderDtoList) {
		
		int storeId = storeService.findStoreIdByMemberId(authentication.getName());
		
		for(ManagerOrderDto dto:managerOrderDtoList) {
			dto.setStoreId(storeId);
		}
		
		System.out.println("-----managerOrder Controller" + managerOrderDtoList.toString());
		managerOrderService.saveManagerOrder(managerOrderDtoList);
		
		return "success";
	}
	
	@GetMapping("/show-orders")
	public List<ManagerOrderWithObj> showOrdersByFilter(int filter){
		List<ManagerOrderWithObj> result = managerOrderService.getManagerOrderByFilter(filter);//수정 : dto로 받기
		return result;
	}
	
	@GetMapping("/show-my-orders")
	public List<ManagerOrderDto> showMyOrder(@RequestParam("memberId") String memberId){
		int storeId = storeService.findStoreIdByMemberId(memberId);
		//수정 존재 안하면 에러 처리
		System.out.println("스토어 아이디" + storeId);
		List<ManagerOrderDto> result = managerOrderService.showMyOrder(storeId);//수정 : dto로 받기
		
		System.out.println("이이이이이 " + result);
		//수정 : 구현
		return result;
		
	}
	
}