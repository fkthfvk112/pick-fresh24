package mart.fresh.com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Store;


public interface StoreRepository extends JpaRepository<Store, Integer>{
	Store findByMemberMemberId(String memberId);
	Store findByStoreId(int storeId);
	Store findByStoreName(String storeName);
	
	@Query("SELECT s FROM Store s " +
            "WHERE s.storeLatitude BETWEEN :minLatitude AND :maxLatitude " +
            "AND s.storeLongitude BETWEEN :minLongitude AND :maxLongitude " +
            "AND s.storeName LIKE CONCAT('%', :partOfStoreName, '%')")
	List<Store> findStoreWitnNByProductName(double minLatitude, double maxLatitude, double minLongitude,double maxLongitude, String partOfStoreName);
}