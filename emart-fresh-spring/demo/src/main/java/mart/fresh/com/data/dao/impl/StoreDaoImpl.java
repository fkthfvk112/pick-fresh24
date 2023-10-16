package mart.fresh.com.data.dao.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.StoreDao;
import mart.fresh.com.data.dto.GetStoreInDisDto;
import mart.fresh.com.data.dto.GetStoreInDisMapDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.dto.StoreDtoWithId;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.data.repository.StoreProductObjRepository;
import mart.fresh.com.data.repository.StoreRepository;
import utils.StoreUtils;

@Component
public class StoreDaoImpl implements StoreDao {
	
	private final StoreProductObjRepository storeProductObjRepository;
	private final StoreRepository storeRepository;
	private final ModelMapper modelmapper;
	
	@Autowired
	public StoreDaoImpl(StoreProductObjRepository storeProductObjRepository, StoreRepository storeRepository, ModelMapper modelMapper) {
		this.storeProductObjRepository = storeProductObjRepository;
		this.storeRepository = storeRepository;
		this.modelmapper = modelMapper;
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
	
	@Override
	public List<StoreDto> getStoreWitnNByProductNameMap(GetStoreInDisMapDto searchDto) {
		
		List<Store> stores = storeProductObjRepository.findStoreByProductNamesMap(
				searchDto.getProductName());
		
		
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

	@Override
	public Store getStoreInfo(int storeId) {
		Optional<Store> os =  storeRepository.findById(storeId);
		if (os.isPresent()) {
		    System.out.println("값 존재 " + os.get());
		    return os.get();
		}
		
		System.out.println("값 없음 ");
		return null;
	}

	@Override
	public int addStore(StoreDtoWithId dto) {
		try {
			Store storeEntity = new Store();
			Member member = new Member();
			member.setMemberId(dto.getMemberId());
			
			storeEntity.setMember(member);
			storeEntity.setStoreAddress(dto.getStoreAddress());
			storeEntity.setStoreLatitude(dto.getStoreLatitude());
			storeEntity.setStoreLongitude(dto.getStoreLongitude());
			storeEntity.setStoreName(dto.getStoreName());
			storeRepository.save(storeEntity);
			
			return 1;
		}catch(Exception e) {
			return 0;
		}
	}
	
	@Override
	public Store findByStoreId(int storeId) {
		return storeRepository.findByStoreId(storeId);
	}

	@Override

	public Store findByMemberMemberId(String memberId) {
		return storeRepository.findByMemberMemberId(memberId);
	}
	
	public Store findStoreByMemberId(String memberId) {
		Store store = storeRepository.findByMemberMemberId(memberId);
		System.out.println("스토어"  +store);
		return store;

	}

	@Override
	public List<Store> getStoreWitnNByProductName(double userLatitude, double userLongitude, int n, String partOfStoreName) {
		double deltaLat = n / 111.0;
        double deltaLon = n / (111.0 * Math.cos(Math.toRadians(userLatitude)));
        
        double minLatitude = userLatitude - deltaLat;
        double maxLatitude = userLatitude + deltaLat;
        double minLongitude = userLongitude - deltaLon;
        double maxLongitude = userLongitude + deltaLon;
        
        return storeRepository.findStoreWitnNByProductName(minLatitude, maxLatitude, minLongitude, maxLongitude, partOfStoreName);
        
        
	}

}