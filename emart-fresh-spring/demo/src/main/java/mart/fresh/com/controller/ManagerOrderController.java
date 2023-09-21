package mart.fresh.com.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.ManagerOrderDto;
import mart.fresh.com.data.entity.ManagerOrderWithObj;
import mart.fresh.com.service.ManagerOrderService;


@RequestMapping("/order")
@RestController
public class ManagerOrderController {
	private final ManagerOrderService managerOrderService;
	
	@Autowired
	public ManagerOrderController(ManagerOrderService managerOrderService) {
		this.managerOrderService = managerOrderService;
	}
	
	@PostMapping("/add-manager-order")
	public String saveManagerOrder(@RequestBody List<ManagerOrderDto> managerOrderDtoList) {
		System.out.println("-----managerOrder Controller" + managerOrderDtoList.toString());
		managerOrderService.saveManagerOrder(managerOrderDtoList);
		
		return "success";
	}
	
	@GetMapping("/show-orders")
	public List<ManagerOrderWithObj> showOrdersByFilter(int filter){
		List<ManagerOrderWithObj> result = managerOrderService.getManagerOrderByFilter(filter);//수정 : dto로 받기
		return result;
	}
	
}