package mart.fresh.com.controller;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import mart.fresh.com.data.dto.ManagerOrderObjDto;
import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;
import mart.fresh.com.service.AdminService;
import mart.fresh.com.service.ManagerOrderService;

@RequestMapping("/admin")
@RestController
public class AdminController {
	
	private final AdminService adminService;
	
	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}
	
	
	@GetMapping("/order-by-date")
	public List<ManagerOrderObjDto> getManagerOrderObjsByDate(String date){
		List<ManagerOrderObjDto> managerOrderWithIdDatas = new ArrayList();
		
		managerOrderWithIdDatas = adminService.getManagerOrderObjsByDate(date);
		System.out.println("날짜 " + managerOrderWithIdDatas);

		return managerOrderWithIdDatas;
	}
	
	@PostMapping("/handle-order")
	public String handleOrder(@RequestBody List<Integer> orderNum) {
		int success = adminService.insertOrderIntoStoreProduct(orderNum);
		if(success == 1) {
			//상태 처리 로직 추가
		}

		return "success";//수정
	}
}
