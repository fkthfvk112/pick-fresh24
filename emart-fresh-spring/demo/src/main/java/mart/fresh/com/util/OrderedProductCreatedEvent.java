package mart.fresh.com.util;


import mart.fresh.com.data.entity.OrderedProduct;

public class OrderedProductCreatedEvent {

    private final OrderedProduct orderedProduct;

    
    // 그냥 OrderedProduct 값 생성
    public OrderedProductCreatedEvent(OrderedProduct orderedProduct) {
    	this.orderedProduct = orderedProduct;
            
    }

    public OrderedProduct getOrderedProduct() {
        return orderedProduct;
    }
}
