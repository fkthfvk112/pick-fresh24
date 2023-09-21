package mart.fresh.com.data.repository;


import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import jakarta.transaction.Transactional;
import mart.fresh.com.data.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, String>{

	@Query("SELECT  r "
		       + "FROM Review r "    
		       + "  WHERE r.member.memberId = :memberId "
			   + " 	ORDER BY r.reviewDate DESC")
	Page<Review> myReviewList(@Param("memberId") String memberId, Pageable pageable);

	@Transactional
	@Modifying
	@Query("DELETE FROM Review r WHERE r.reviewId = :reviewId")
	int deleteByReviewId(@Param("reviewId") int reviewId);
	
	@Query("SELECT r " +
		       "FROM Review r " +
		       "WHERE r.productTitle = :productTitle " +
		       "ORDER BY " +
		       "    CASE WHEN :select = 0 THEN r.reviewDate ELSE null END DESC, " +
		       "    CASE WHEN :select = 1 THEN r.reviewScore ELSE null END DESC,  " +
		       " 	CASE WHEN :select = 1 THEN r.reviewDate ELSE null END DESC, " +
		       "    CASE WHEN :select = 2 THEN r.reviewScore ELSE null END ASC, "
		       + "	CASE WHEN :select = 2 THEN r.reviewDate ELSE null END DESC ")
	List<Review> getProductReviewByProductTitle(
			@Param("productTitle") String productTitle, 
			@Param("select") int select);
}
