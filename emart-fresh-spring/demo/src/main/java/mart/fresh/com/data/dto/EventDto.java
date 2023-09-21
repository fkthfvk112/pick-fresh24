package mart.fresh.com.data.dto;

import java.sql.Timestamp;


import lombok.Data;

@Data
public class EventDto {
    private int eventId;
    private String eventTitle;
    private String eventBannerImage;
    private String eventDetailImage;
    private Timestamp eventStartDate;
    private Timestamp eventEndDate;
    private int eventListCount;
}