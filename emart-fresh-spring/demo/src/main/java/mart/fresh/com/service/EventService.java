package mart.fresh.com.service;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import mart.fresh.com.data.dto.EventDto;

public interface EventService {
	boolean eventUpdate(EventDto dto);

	Page<EventDto> eventList(int page, int size);

	String uploadImage(MultipartFile eventDetailImage) throws Exception;

	EventDto eventDetail(int eventId);

	List<EventDto> nowEventList();

}