package mart.fresh.com.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.dto.StoreDtoWithId;
import mart.fresh.com.data.entity.Store;

public interface StoreService {
	List<StoreDto> getStoreWitnNByProductName(GetStoreInDisDto dto);
	StoreDto getStoreInfo(int storeId);
	int addStore(@RequestBody StoreDtoWithId dto);
	Store findByStoreId(int storeId);
}
