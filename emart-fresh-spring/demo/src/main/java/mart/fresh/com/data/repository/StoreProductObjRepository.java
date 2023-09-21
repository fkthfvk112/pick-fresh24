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

}
