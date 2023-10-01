package mart.fresh.com.data.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;



public interface OrderedProductProductRepository extends JpaRepository<OrderedProductProduct, Integer> {

//	@Query("SELECT opp " 
//	        + "FROM OrderedProductProduct opp "
//	        + "JOIN opp.orderedProduct op " 
//	        + "WHERE op.store.storeId = :storeId "
//	        + "AND op.isPickup = :isPickup "
//	        + "ORDER BY op.orderedDate DESC")
//	List<OrderedProductProduct> getOrderedListByStoreId(@Param("storeId") int storeId, @Param("isPickup") boolean isPickup);

//	  @Query("SELECT opp FROM OrderedProductProduct opp JOIN opp.orderedProduct op WHERE op.orderedProductId = :orderedProductId")
	  
//	  	// 여기서 터지는듯
//	    OrderedProductProduct findByorderedProductId(int orderedProductId);

}
 