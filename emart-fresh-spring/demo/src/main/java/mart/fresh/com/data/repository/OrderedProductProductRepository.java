package mart.fresh.com.data.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import mart.fresh.com.data.entity.OrderedProductProduct;


public interface OrderedProductProductRepository extends JpaRepository<OrderedProductProduct, Integer> {

	@Query("SELECT opp "
	        + "FROM OrderedProductProduct opp "
			+ "JOIN opp.orderedProduct op "
	        + "WHERE op.member.memberId = :memberId "
			+ "AND op.orderedDel = :orderedDel "
			+ "ORDER BY op.orderedDate DESC")
	Page<OrderedProductProduct> getOrderedProductProductByMemberId(@Param("memberId") String memberId,
																	@Param("orderedDel") boolean orderedDel,Pageable pageable);
	
	
	
	
	@Query("SELECT opp "
	        + "FROM OrderedProductProduct opp "
			+ "JOIN opp.orderedProduct op "
	        + "WHERE op.store.storeId = :storeId "
			+ "AND op.isPickup = :isPickup "
			+ "ORDER BY op.orderedDate DESC")
	Page<OrderedProductProduct> getOrderedListByStoreId(@Param("storeId") int storeId,
																	@Param("isPickup") boolean isPickup,Pageable pageable);
	List<OrderedProductProduct> findByOrderedProductOrderedProductId(int orderedProductId);

}
 