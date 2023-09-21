package mart.fresh.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mart.fresh.com.data.dto.ReviewDto;
import mart.fresh.com.service.ProductService;
import mart.fresh.com.data.dto.ReviewDto;
import mart.fresh.com.service.ReviewService;

@RequestMapping("/review")
@RestController
public class ReviewController {

	private final ReviewService reviewService;
	
	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}
	
	@PostMapping("/review-list")
	public Page<ReviewDto> myReviewList(@RequestParam String memberId,
										@RequestParam int page, @RequestParam int size) {
		
		Page<ReviewDto> reviewList = reviewService.myReviewList(memberId, page-1, size);
		return reviewList;
	}
	

	@PostMapping("/review-delete")
	public String myReviewDelete(@RequestParam int reviewId) {
		
		boolean isS = reviewService.myReviewDelete(reviewId);
		
		if(isS) { return "삭제"; }
		else { return "삭제실패"; }
	}
	

	@GetMapping("/product-review")
	//select : 정렬순서 0 최신순 1 평점 높은순 2 평점 낮은 순 
	public List<ReviewDto> getProductReviewByProductTitle(String productTitle, @RequestParam(defaultValue = "0") int select){
		
		return reviewService.getProductReviewByProductTitle(productTitle, select);
	}

}