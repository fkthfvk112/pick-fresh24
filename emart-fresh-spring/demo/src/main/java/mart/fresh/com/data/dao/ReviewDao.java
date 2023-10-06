package mart.fresh.com.data.dao;

import java.util.List;

import org.springframework.data.domain.Page;
import mart.fresh.com.data.entity.Review;

public interface ReviewDao {
	Page<Review> myReviewList(String memberId, int page, int size);
	int myReviewDelete(int reviewId);
	List<Review> getProductReviewByProductTitle(String productTitle, int select);
	void save(Review review);
}