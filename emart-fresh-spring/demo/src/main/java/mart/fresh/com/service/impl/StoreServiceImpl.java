package mart.fresh.com.service.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dao.StoreDao;
import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.dto.StoreDtoWithId;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.service.StoreService;

@Service
public class StoreServiceImpl implements StoreService {
	private final StoreDao storeDao;
	private final ModelMapper modelMapper;
	
	@Autowired
	public StoreServiceImpl(StoreDao storeDao, ModelMapper modelMapper) {
		this.storeDao = storeDao;
		this.modelMapper = modelMapper;
	}//엔티티 객체 DTO객체 전환


	@Override
	public List<StoreDto> getStoreWitnNByProductName(GetStoreInDisDto dto) {
		return storeDao.getStoreWitnNByProductName(dto) ;
	}


	@Override
	public StoreDto getStoreInfo(int storeId) {
		Store store = storeDao.getStoreInfo(storeId);
		StoreDto storeDto = modelMapper.map(store, StoreDto.class);
				
		System.out.println("스토어 dto" +  storeDto);
		
		return storeDto;
	}


	@Override
	public int addStore(StoreDtoWithId dto) {
		
		return storeDao.addStore(dto);
	}
	
	@Override
	public Store findByStoreId(int storeId) {
		return storeDao.findByStoreId(storeId);
	}
}

