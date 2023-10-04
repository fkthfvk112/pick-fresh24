package mart.fresh.com.util;

import jakarta.persistence.PostPersist;
import mart.fresh.com.data.entity.OrderedProduct;

public class OrderedProductListener {
	
	//Entity Listener 동작 시 EventPublisherHolder에 OrderedProductCreatedEvent 입력
    @PostPersist
    public void onOrderedProductCreate(OrderedProduct orderedProduct) {
        EventPublisherHolder.getPublisher().publishEvent(new OrderedProductCreatedEvent(orderedProduct));
    }
}
