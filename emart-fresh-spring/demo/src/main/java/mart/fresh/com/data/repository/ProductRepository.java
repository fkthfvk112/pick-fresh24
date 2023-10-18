package mart.fresh.com.data.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;

public interface ProductRepository extends JpaRepository<Product, Long>{
	 @Query("SELECT s "
	            + "FROM Store s "
	            + "WHERE s.storeId IN ( "
	            + "    SELECT sp.store.storeId "
	            + "    FROM Product p "
	            + "    JOIN StoreProduct sp ON p.productId = sp.product.productId "
	            + "    WHERE p.productTitle = :productName) "
	            )
	List<Store> getNearStoreInfos(@Param("productName") String productName);
	 
	 @Query("SELECT sp.product "
		       + "FROM StoreProduct AS sp "
		       + "WHERE sp.store.storeId = :storeId")
	List<Product> getProductsByStoreId(@Param("storeId") int storeId);
	 
	 
	 //수정...?
	 @Query("SELECT p FROM Product p "
		       + "WHERE p.productTitle LIKE CONCAT('%', :searchingTerm, '%') "
		       + "AND (:eventNumber = 0 OR p.productEvent = :eventNumber) "
		       + "ORDER BY "
		       + "    CASE WHEN :select = 0 THEN p.createdAt ELSE null END DESC, "
		       + "    CASE WHEN :select = 1 THEN p.priceNumber ELSE null END ASC, "
		       + "    CASE WHEN :select = 2 THEN p.priceNumber ELSE null END DESC")
	List<Product> getProductDtoListByFilter(
			@Param("searchingTerm") String searchingTerm,
			@Param("eventNumber") int eventNumber,
			@Param("select") int select,
			Pageable pageable
			);
	 
	 @Query("SELECT p FROM Product p "
		       + "WHERE p.productTitle LIKE CONCAT('%', :searchingTerm, '%') "
		       + "AND (:eventNumber = 0 OR p.productEvent = :eventNumber)")
		List<Product> getProductDtoListByFilterNotPagable(
		     @Param("searchingTerm") String searchingTerm,
		     @Param("eventNumber") int eventNumber);

	 
	 @Query("SELECT sp.product FROM StoreProduct sp "
		       + "WHERE sp.store.storeId = :storeId "
		       + "AND sp.product.productTitle LIKE CONCAT('%', :searchingTerm, '%') "
		       + "AND (:eventNumber = 0 OR sp.product.productEvent = :eventNumber) "
		       + "ORDER BY "
		       + "    CASE WHEN :select = 0 THEN sp.product.createdAt ELSE null END DESC, "
		       + "    CASE WHEN :select = 1 THEN sp.product.priceNumber ELSE null END ASC, "
		       + "    CASE WHEN :select = 2 THEN sp.product.priceNumber ELSE null END DESC")
	 List<Product> getStoreProductDtoListByFilter(
			@Param("searchingTerm") String searchingTerm,
			@Param("eventNumber") int eventNumber,
			@Param("select") int select,
			@Param("storeId") int storeId,
			Pageable pageable
			);
	 
	 @Query("SELECT p.productImgUrl FROM Product p WHERE p.productTitle = :productTitle")
	 List<String> getProductImgUrlsByProductTitle(String productTitle);
	 
	 
	 @Query("SELECT p FROM Product p WHERE p.productExpirationDate <= :sixHoursFromNow")
	 List<Product> findProductTimeSale(LocalDateTime sixHoursFromNow);


	Product findByProductId(int productId);

	List<Product> findByProductTitle(String productTitle);
	 
}

