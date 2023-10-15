package mart.fresh.com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.entity.StoreProduct;

public interface StoreProductObjRepository extends JpaRepository<StoreProduct, Integer> {
	
	@Query("SELECT sp.store FROM StoreProduct sp WHERE sp.product.productTitle = :productName")
	List<Store> findStoreByProductName(@Param("productName") String productName);

	@Query("SELECT sp.store FROM StoreProduct sp " +
		       "WHERE sp.product.productTitle IN :productNames " +
		       "GROUP BY sp.store.storeId " +
		       "HAVING COUNT(DISTINCT sp.product.productTitle) = :productSize")
	List<Store> findStoreByProductNames(
			@Param("productNames") List<String> productNames,
			@Param("productSize") int productSize);
	
	@Query("SELECT DISTINCT sp.store FROM StoreProduct sp " +
		       "WHERE (:productName IS NULL OR :productName = '' OR sp.product.productTitle = :productName)")
	List<Store> findStoreByProductNamesMap(@Param("productName") String productName);
	
	
	@Query("SELECT sp "
			+ "FROM StoreProduct sp"
			+ " JOIN FETCH sp.product "
			+ "WHERE sp.store.storeId = :storeId ")
	List<StoreProduct> findtStoreProuctByStoreId(@Param("storeId") int storeId);

	StoreProduct findByStoreStoreIdAndProductProductId(int storeId, Integer productId);
}
