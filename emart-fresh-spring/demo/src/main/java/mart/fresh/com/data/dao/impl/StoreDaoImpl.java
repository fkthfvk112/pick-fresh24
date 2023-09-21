package mart.fresh.com.data.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.StoreDao;
import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.data.repository.StoreProductObjRepository;
import utils.StoreUtils;

@Component
public class StoreDaoImpl implements StoreDao {
	
	private final StoreProductObjRepository storeProductObjRepository;
	
	@Autowired
	public StoreDaoImpl(StoreProductObjRepository storeProductObjRepository) {
		this.storeProductObjRepository = storeProductObjRepository;
	}
	
	@Override
	public List<StoreDto> getStoreWitnNByProductName(GetStoreInDisDto searchDto) {
		List<Store> stores = storeProductObjRepository.findStoreByProductNames(
				searchDto.getProductNames(),
				searchDto.getProductNames().size());
		
		
		List<StoreDto> storeDtos = new ArrayList();
		
		for(Store store:stores) {
			double storeLatitude = store.getStoreLatitude();
			double storeLongitude = store.getStoreLongitude();
			double distance = StoreUtils.haversineDistance(
					storeLatitude, storeLongitude, searchDto.getUserLatitude(), searchDto.getUserLongitude()
					);
			
			if(distance <= searchDto.getMaxDis()) {
				StoreDto dto = new StoreDto();
				
				dto.setStoreId(store.getStoreId());
				dto.setStoreName(store.getStoreName());
				dto.setStoreAddress(store.getStoreAddress());
				// dto.setMemberId(store.getMemberId());
				dto.setStoreLatitude(store.getStoreLatitude());
				dto.setStoreLongitude(store.getStoreLongitude());		
				storeDtos.add(dto);
			}
		}
		
		System.out.println("-----getStoreWitnNByProductName" + storeDtos);
		
		return storeDtos;
	}

}