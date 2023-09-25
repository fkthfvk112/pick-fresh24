package mart.fresh.com.util;

import java.util.List;

import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;

public class OrderedProductCreatedEvent {

    private final OrderedProduct orderedProduct;

    
    // 그냥 OrderedProduct 값 생성
    public OrderedProductCreatedEvent(OrderedProduct orderedProduct) {
        System.out.println("OrderedProductCreatedEvent 값 확인 : " + orderedProduct.toString() );
    	this.orderedProduct = orderedProduct;
            
    }

    public OrderedProduct getOrderedProduct() {
    	System.out.println("OrderedProductCreatedEvent getOrderedProduct 값 확인 : " + orderedProduct.toString() );
        return orderedProduct;
    }
}
