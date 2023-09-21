package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ReviewDto {

    private int reviewId;
    private String memberId;
    private String productTitle;
    private String reviewContent;
    private Timestamp reviewDate;
    private int reviewScore;
    private String productImgUrl;
    
}