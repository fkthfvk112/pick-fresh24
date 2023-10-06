package mart.fresh.com.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import mart.fresh.com.data.dto.EventDto;
import mart.fresh.com.service.EventService;

@RequestMapping("/event")
@RestController
public class EventController {
    private final EventService eventService;

    @Autowired
    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/event-list")
    public ResponseEntity<Page<EventDto>> eventList(@RequestParam int page, @RequestParam int size) {
    	   Page<EventDto> eventList = eventService.eventList(page, size);
           if(eventList != null && !eventList.isEmpty()) {
   			  return ResponseEntity.ok(eventList);
           } else {
           	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
   	    }
    }
    
    @GetMapping("/now-event-list")
    public ResponseEntity<List<EventDto>> nowEventList() {
    	List<EventDto> eventList = eventService.nowEventList();
        if(eventList != null && !eventList.isEmpty()) {
			  return ResponseEntity.ok(eventList);
        } else {
        	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
        
    }
    
    @GetMapping("/detail")
    public ResponseEntity<EventDto> eventList(@RequestParam int eventId) {
        EventDto eventDetail = eventService.eventDetail(eventId);
        if(eventDetail != null) {
			  return ResponseEntity.ok(eventDetail);
      } else {
      	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
	    }
      
    }
    
    
    @PostMapping("/event-update")
    public String eventUpdate(@RequestParam("event_title") String eventTitle,
            @RequestParam("event_banner_image") MultipartFile eventBannerImage,
            @RequestParam("event_detail_image") MultipartFile eventDetailImage,
            @RequestParam("event_start_date") String eventStartDate,
            @RequestParam("event_end_date") String eventEndDate) throws IOException {

        System.out.println("EventController eventUpdate");

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startParsedDate = dateFormat.parse(eventStartDate);
            Date endParsedDate = dateFormat.parse(eventEndDate);

            Timestamp startTimestamp = new Timestamp(startParsedDate.getTime());
            Timestamp endTimestamp = new Timestamp(endParsedDate.getTime());

            EventDto dto = new EventDto();
            dto.setEventTitle(eventTitle);

            // 이미지 업로드 및 URL 설정
            String bannerImageUrl = eventService.uploadImage(eventBannerImage);
            String detailImageUrl = eventService.uploadImage(eventDetailImage);

            if (bannerImageUrl != null && detailImageUrl != null) {
                dto.setEventBannerImage(bannerImageUrl);
                dto.setEventDetailImage(detailImageUrl);
                dto.setEventStartDate(startTimestamp);
                dto.setEventEndDate(endTimestamp);

                System.out.println("startTimestamp : " + startTimestamp + " endTimestamp : " + endTimestamp
                        + " eventStartDate : " + eventStartDate);

                boolean saveSuccess = eventService.eventUpdate(dto);

                if (saveSuccess) {
                    return "success";
                } else {
                    return "fail";
                }
            } else {
                return "fail"; // 이미지 업로드 실패
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "fail";
        }
    }
}