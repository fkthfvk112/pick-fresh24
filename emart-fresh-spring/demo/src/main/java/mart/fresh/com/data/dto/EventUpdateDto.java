package mart.fresh.com.data.dto;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class EventUpdateDto {
	private String eventTitle;
    private MultipartFile eventBannerImage;
    private MultipartFile eventDetailImage;
    private String eventStartDate;
    private String eventEndDate;
	
}
