package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class ReviewSummaryDto {
	
	private String productTitle;
    private Double avgReviewScore;
    
    public ReviewSummaryDto(String productTitle, Double avgReviewScore) {
        this.productTitle = productTitle;
        this.avgReviewScore = avgReviewScore;
    }

}
