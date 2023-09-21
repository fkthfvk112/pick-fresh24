package mart.fresh.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dao.StoreDao;
import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {
	private final StoreDao storeDao;
	
	@Autowired
	public StoreServiceImpl(StoreDao storeDao) {
		this.storeDao = storeDao;
	}//엔티티 객체 DTO객체 전환


	@Override
	public List<StoreDto> getStoreWitnNByProductName(GetStoreInDisDto dto) {
		return storeDao.getStoreWitnNByProductName(dto) ;
	}
	
	
	
}

