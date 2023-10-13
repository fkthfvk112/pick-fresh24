package mart.fresh.com.data.dao.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.ReviewDao;
import mart.fresh.com.data.dto.ReviewSummaryDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Review;
import mart.fresh.com.data.repository.ReviewRepository;

@Component
public class ReviewDaoImpl implements ReviewDao{

	private final ReviewRepository reviewRepository;
	
	@Autowired
	public ReviewDaoImpl(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}
	
	@Override
	public Page<Review> myReviewList(String memberId, int page, int size) {
		System.out.println("ReviewDaoImpl MyReviewList");
		
		Pageable pageable = PageRequest.of(page, size);
		
		Page<Review> reviewList = reviewRepository.myReviewList(memberId, pageable);
		
		return reviewList;
	}

	
	@Override
	public int myReviewDelete(int reviewId) {
		return reviewRepository.deleteByReviewId(reviewId);
		}
	

	@Override
	public List<Review> getProductReviewByProductTitle(String productTitle, int select) {
		List<Review> reviewList = reviewRepository.getProductReviewByProductTitle(productTitle, select);
		System.out.println("-----getProductReviewByProductTitle");
		System.out.println(reviewList);

		return reviewList;
	}

	@Override
	public void save(Review review) {
		reviewRepository.save(review);
	}

	@Override
	public List<ReviewSummaryDto> findTopNProductsByReviewScore(int n) {
        List<ReviewSummaryDto> topProductList = reviewRepository.findTopNProductsByReviewScore(n);
		return topProductList.subList(0, Math.min(n, topProductList.size()));
	}
	
}