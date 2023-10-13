package mart.fresh.com.data.dao;

import java.util.List;

import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.dto.StoreDtoWithId;
import mart.fresh.com.data.entity.Store;

public interface StoreDao {
	List<StoreDto> getStoreWitnNByProductName(GetStoreInDisDto dto);
	Store getStoreInfo(int storeId);
	int addStore(StoreDtoWithId dto);
	public Store findByStoreId(int storeId);
<<<<<<< HEAD
	Store findByMemberMemberId(String memberId);
=======
	Store findStoreByMemberId(String memberId);
>>>>>>> c1440f529009d83a02d8bdf5778b336260303f3c
}
