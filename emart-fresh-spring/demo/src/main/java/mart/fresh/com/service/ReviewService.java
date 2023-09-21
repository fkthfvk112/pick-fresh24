package mart.fresh.com.service;

import java.util.List;

import org.springframework.data.domain.Page;

import mart.fresh.com.data.dto.ReviewDto;

public interface ReviewService {
	Page<ReviewDto> myReviewList(String memberId, int page, int size);
	boolean myReviewDelete(int reviewId);
	List<ReviewDto> getProductReviewByProductTitle(String productTitle, int select);

}