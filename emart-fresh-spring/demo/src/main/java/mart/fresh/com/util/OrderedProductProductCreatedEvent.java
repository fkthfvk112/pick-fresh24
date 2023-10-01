//package mart.fresh.com.util;
//
//import java.util.List;
//
//import mart.fresh.com.data.entity.OrderedProduct;
//import mart.fresh.com.data.entity.OrderedProductProduct;
//
//public class OrderedProductProductCreatedEvent {
//
//    private final OrderedProductProduct orderedProductProduct;
//
//    
//    // 그냥 OrderedProduct 값 생성
//    public OrderedProductProductCreatedEvent(OrderedProductProduct orderedProductProduct) {
//        System.out.println("OrderedProductProductCreatedEvent 값 확인 : " + orderedProductProduct.toString() );
//        
//        
//        
//    	this.orderedProductProduct = orderedProductProduct;
//            
//    }
//
//    public OrderedProductProduct getOrderedProductProduct() {
//    	System.out.println("OrderedProductCreatedEvent getOrderedProduct 값 확인 : " + orderedProductProduct.toString() );
//        return orderedProductProduct;
//    }
//}
