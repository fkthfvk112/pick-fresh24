package mart.fresh.com.data.dao;

import java.util.List;

import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Store;

public interface StoreDao {
	List<StoreDto> getStoreWitnNByProductName(GetStoreInDisDto dto);
	Store getStoreInfo(int storeId);
}
