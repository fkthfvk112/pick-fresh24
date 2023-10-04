package mart.fresh.com.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import mart.fresh.com.data.entity.Event;

public interface EventRepository extends JpaRepository<Event, Integer>{


	 @Query("SELECT e "
	 		+ "FROM Event e "
			+ "ORDER BY e.eventStartDate DESC")
	 Page<Event> eventList(Pageable pageable);
 
	@Query("SELECT COUNT(e) "
	        + "FROM Event e ")
	int eventListCount();

	Event findByEventId(int eventId);
}