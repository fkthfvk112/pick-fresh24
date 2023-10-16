package mart.fresh.com.data.repository;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mart.fresh.com.data.dto.OrderedCountDto;
import mart.fresh.com.data.entity.OrderedProductProduct;

public interface OrderedProductProductRepository extends JpaRepository<OrderedProductProduct, Integer> {

	List<OrderedProductProduct> findByOrderedProductOrderedProductId(int orderedProductId);

	int countByOrderedProductOrderedProductId(int orderedProductId);

	@Query("SELECT COUNT(DISTINCT opp.orderedProduct.orderedProductId) " + "FROM OrderedProductProduct opp "
			+ "WHERE opp.orderedProduct.member.memberId = :memberId")
	long countUniqueOrderedProductsByMemberId(String memberId);

	OrderedProductProduct findByOrderedProductProductId(int orderedProductProductId);

	@Query("SELECT new mart.fresh.com.data.dto.OrderedCountDto(p.productTitle, COUNT(*)) "
			+ "FROM OrderedProductProduct opp " + "INNER JOIN opp.product p " + "GROUP BY p.productTitle "
			+ "ORDER BY COUNT(*) DESC")
	List<OrderedCountDto> findProductsByOrderedCount(int n);
	
	List<OrderedProductProduct> findByOrderedProductStoreStoreIdAndOrderedProductOrderedDateBetweenOrderByOrderedProductOrderedDateAsc(int storeId, Timestamp startDate, Timestamp endDate);
}
