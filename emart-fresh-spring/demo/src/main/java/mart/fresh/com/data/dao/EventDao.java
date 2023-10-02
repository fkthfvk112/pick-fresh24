package mart.fresh.com.data.dao;

import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.EventDto;
import mart.fresh.com.data.entity.Event;

public interface EventDao {
	boolean eventUpdate(EventDto dto);
	Page<Event> eventList(int page, int size);
	Event eventDetail(int eventId);
}