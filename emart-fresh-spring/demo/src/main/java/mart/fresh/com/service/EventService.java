package mart.fresh.com.service;



import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;
import mart.fresh.com.data.dto.EventDto;


public interface EventService {
	boolean eventUpdate(EventDto dto);

	Page<EventDto> eventList(int page, int size);

	String uploadImage(MultipartFile eventDetailImage) throws Exception;

}