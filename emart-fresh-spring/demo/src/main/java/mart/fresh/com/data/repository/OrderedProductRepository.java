package mart.fresh.com.data.repository;

import java.util.Date;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;

public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Integer> {

	@Query("SELECT COUNT(op) " + "FROM OrderedProduct op " + "WHERE op.member.memberId = :memberId")
	int myOrderedCount(@Param("memberId") String memberId);

	@Query("SELECT opp " + "FROM OrderedProductProduct opp " + "JOIN opp.orderedProduct op "
			+ "WHERE op.member.memberId = :memberId " + "AND op.orderedDel = :orderedDel "
			+ "ORDER BY op.orderedDate DESC")
	Page<OrderedProductProduct> getOrderedProductProductByMemberId(@Param("memberId") String memberId,
			@Param("orderedDel") boolean orderedDel, Pageable pageable);

	OrderedProduct findByIsPickupAndMemberMemberId(boolean isPickup, String memberId);

	List<OrderedProduct> findByMemberMemberId(String memberId);

	@Query("SELECT CASE WHEN COUNT(op) > 0 THEN true ELSE false END " + "FROM OrderedProduct op "
			+ "WHERE op.orderedDate > :currentTime")
	boolean isafterByOrderedProductOrderedDate(@Param("currentTime") Date date);

	List<OrderedProduct> findByIsPickupAndStoreStoreId(boolean isPickup, int storeId);

	OrderedProduct findByOrderedProductId(int orderedProductId);

}