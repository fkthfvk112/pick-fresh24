package mart.fresh.com.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import mart.fresh.com.data.dao.ReviewDao;
import mart.fresh.com.data.dto.ReviewDto;
import mart.fresh.com.data.entity.Review;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.service.ReviewService;

@Service
public class ReviewServiceImpl implements ReviewService {
    private final ReviewDao reviewDao;
    private final ProductRepository productRepository;

    @Autowired
    public ReviewServiceImpl(ReviewDao reviewDao, ProductRepository productRepository) {
        this.reviewDao = reviewDao;
        this.productRepository = productRepository;
    }

    private ReviewDto convertEntityToDto(Review entity) {
        ReviewDto dto = new ReviewDto();
   	 	String productImgUrl = productRepository.getProductImgUrlByProductTitle(entity.getProductTitle());
        
        dto.setProductImgUrl(productImgUrl);
        dto.setReviewId(entity.getReviewId());
        dto.setMemberId(entity.getMember().getMemberId()); 
        dto.setProductTitle(entity.getProductTitle());
        dto.setReviewContent(entity.getReviewContent());
        dto.setReviewDate(entity.getReviewDate());
        dto.setReviewScore(entity.getReviewScore());
        
        return dto;
    }

    @Override
    public Page<ReviewDto> myReviewList(String memberId, int page, int size) {
        System.out.println("ReviewServiceImpl myReviewList");

        Page<Review> reviewList = reviewDao.myReviewList(memberId, page, size);
        Page<ReviewDto> reviewDtoList = reviewList.map(this::convertEntityToDto);
      
        return reviewDtoList;
    }

    @Override
    public boolean myReviewDelete(int reviewId) {
    	int myReviewDelete = reviewDao.myReviewDelete(reviewId);
    	return myReviewDelete>0?true:false;
    }
    
    
    
    
	@Override
	public List<ReviewDto> getProductReviewByProductTitle(String productTitle, int select) {//수정
		List<Review> reviewList = reviewDao.getProductReviewByProductTitle(productTitle, select);
		List<ReviewDto> reviewDtoList = new ArrayList();
		
		for(Review review:reviewList) {
			ReviewDto reviewDto = convertEntityToDto(review);
            reviewDtoList.add(reviewDto);
		}
		
		return reviewDtoList;
	}

}