package mart.fresh.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.service.StoreService;

@RequestMapping("/store")
@RestController
public class StoreContoller {
private final StoreService storeService;
	
	@Autowired
	public StoreContoller(StoreService storeService) {
		this.storeService = storeService;
	}
	
	@PostMapping("/get-store-witnin-n")
	public List<StoreDto> getStoreWitnNByProductName(@RequestBody GetStoreInDisDto dto) {
		if(dto == null || dto.getProductNames().size() == 0) return  null;
		System.out.println("파람" + dto.toString());
		List<StoreDto> nearStoreList = storeService.getStoreWitnNByProductName(dto);
		
		return nearStoreList;
	}
}
