package mart.fresh.com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.Store;



public interface OrderedProductRepository extends JpaRepository<OrderedProduct, Integer> {

	@Query("SELECT COUNT(op) "
			+ "FROM OrderedProduct op "
			+ "WHERE op.member.memberId = :memberId")
	int myOrderedCount(@Param("memberId") String memberId);
	

	@Query("SELECT op.store "
	        + "FROM OrderedProduct op "
	        + "JOIN op.store s "
	        + "WHERE op.member.memberId = :memberId")
	Store findByMemberId(@Param("memberId") String memberId);

	List<OrderedProduct> findByMemberMemberId(String memberId);

	
}