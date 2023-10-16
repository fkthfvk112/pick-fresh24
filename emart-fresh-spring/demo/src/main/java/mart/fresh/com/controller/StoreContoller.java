package mart.fresh.com.controller;

import java.util.List;
import org.springframework.http.HttpStatus;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.GetStoreRequestDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.dto.StoreDtoWithId;
import mart.fresh.com.data.dto.StoreListDto;
import mart.fresh.com.data.entity.Store;
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
	
	@GetMapping("/get-storeinfo")
	public ResponseEntity<StoreDto> getStoreInfo(int storeId) {
		StoreDto storeDto = storeService.getStoreInfo(storeId);
		if(storeDto == null) {
			return ResponseEntity
					.status(HttpStatus.NOT_FOUND)
	                .body(storeDto);
		}
		return ResponseEntity.ok(storeDto);
	}
	
	@PostMapping("/add-store")
	public ResponseEntity<String> addStore(@RequestBody StoreDtoWithId dto){
		int result = storeService.addStore(dto);
		
		if(result == 1) {
			return ResponseEntity.ok().body("success");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
		}
	}
	
	@GetMapping("/get-storeList")
    public ResponseEntity<List<StoreListDto>> getStoresWithNANDStoreName(@ModelAttribute GetStoreRequestDto requestDto) {
        List<StoreListDto> storeList = storeService.getStoresWithNANDStoreName(
                requestDto.getUserLatitude(),
                requestDto.getUserLongitude(),
                requestDto.getN(),
                requestDto.getPartOfStoreName()
        );

        return ResponseEntity.ok(storeList);
    }
}
