package mart.fresh.com.data.dao.impl;


import java.sql.Timestamp;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import mart.fresh.com.data.dao.EventDao;
import mart.fresh.com.data.dto.EventDto;
import mart.fresh.com.data.entity.Event;
import mart.fresh.com.data.repository.EventRepository;

@Component
public class EventDaoImpl implements EventDao {

    private final EventRepository eventRepository;

    @Autowired
    public EventDaoImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }
    
    private Event convertDtoToEvent(EventDto dto) {
    	

        Timestamp eventEndDate = dto.getEventEndDate();
        
        if(eventEndDate != null) {
	        Calendar calendar = Calendar.getInstance();
	        calendar.setTimeInMillis(eventEndDate.getTime());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			eventEndDate.setTime(calendar.getTimeInMillis());
        }
        
        Event eventEntity = new Event();
        eventEntity.setEventTitle(dto.getEventTitle());
        eventEntity.setEventBannerImage(dto.getEventBannerImage());
        eventEntity.setEventDetailImage(dto.getEventDetailImage());
        eventEntity.setEventStartDate(dto.getEventStartDate());
        eventEntity.setEventEndDate(eventEndDate);
        return eventEntity;
    }
    
    @Override
    public boolean eventUpdate(EventDto dto) {
        System.out.println("EventDaoImpl eventUpdate");
        
        Event eventEntity = convertDtoToEvent(dto);
        Event savedEvent = eventRepository.save(eventEntity);
        
        return savedEvent != null;
    }


    @Override
    public Page<Event> eventList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
    	return eventRepository.eventList(pageable);
    }

	@Override
	public Event eventDetail(int eventId) {
		Event event = eventRepository.findByEventId(eventId); 
		return event;
	}

	@Override
	public List<Event> nowEventList() {
        Timestamp currentTime = new Timestamp(System.currentTimeMillis());
        return eventRepository.findByEventEndDateAfterOrderByEventEndDateAsc(currentTime);
	}
    
}