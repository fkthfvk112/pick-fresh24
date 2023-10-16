package mart.fresh.com.service;

import java.util.List;

import org.springframework.web.bind.annotation.RequestBody;

import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.GetStoreInDisMapDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.dto.StoreDtoWithId;
import mart.fresh.com.data.dto.StoreListDto;
import mart.fresh.com.data.entity.Store;

public interface StoreService {
	List<StoreDto> getStoreWitnNByProductName(GetStoreInDisDto dto);
	List<StoreDto> getStoreWitnNByProductNameMap(GetStoreInDisMapDto dto);
	StoreDto getStoreInfo(int storeId);
	int addStore(@RequestBody StoreDtoWithId dto);
	Store findByStoreId(int storeId);
	int findStoreIdByMemberId(String storeId);
	List<StoreListDto> getStoresWithNANDStoreName(double userLatitude, double userLongitude, int n, String partOfStoreName); 
}
