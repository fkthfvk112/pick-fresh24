package mart.fresh.com.data.dto;

import lombok.Data;
import mart.fresh.com.data.entity.Review;

@Data
public class ReviewRequestDto {
	private Review review;
	private int orderedProductProductId;
}
