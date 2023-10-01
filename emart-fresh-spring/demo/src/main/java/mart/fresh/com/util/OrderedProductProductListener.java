//package mart.fresh.com.util;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PostPersist;
//import mart.fresh.com.data.entity.OrderedProductProduct;
//
//public class OrderedProductProductListener {
//	
//	 private EntityManager entityManager;
//	 
//	//Entity Listener 동작 시 EventPublisherHolder에 OrderedProductCreatedEvent 입력
//    @PostPersist
//    public void onOrderedProductProductCreate(OrderedProductProduct OrderedProductProduct) {
//        EventPublisherHolder.getPublisher().publishEvent(new OrderedProductProductCreatedEvent(OrderedProductProduct));
//        
//        System.out.println("onOrderedProductCreate orderedProduct 값 확인 : " + OrderedProductProduct.toString());
//    }
//}
