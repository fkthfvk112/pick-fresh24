package mart.fresh.com.service;


import java.util.List;
import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.util.OrderedProductCreatedEvent;
import reactor.core.publisher.Flux;
import mart.fresh.com.data.entity.OrderedProduct;



public interface OrderedProductService {
	Page<MyOrderedProductDto> getOrderedProductByMemberId(String memberId, int page, int size);
	Flux<MyOrderedProductDto> streamOrderedProductsByStoreId(String memberId);
	void handleOrderedProductCreatedEvent(OrderedProductCreatedEvent event);
	void saveOrderedProduct(OrderedProduct orderedProduct);
	List<OrderedProduct> findByMemberMemberId(String memberId);
	OrderedProduct findByOrderedProductId(int orderedProductId);

}