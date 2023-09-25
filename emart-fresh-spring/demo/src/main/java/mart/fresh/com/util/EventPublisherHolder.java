package mart.fresh.com.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
public class EventPublisherHolder {
    private static ApplicationEventPublisher publisher;

    
    // 이벤트 발생? 시키는 부분
    @Autowired
    public EventPublisherHolder(ApplicationEventPublisher publisher) {
        EventPublisherHolder.publisher = publisher;
        
        System.out.println("EventPublisherHolder 확인 : " + publisher.toString() );
    }

    public static ApplicationEventPublisher getPublisher() {
        return publisher;
    }
}
