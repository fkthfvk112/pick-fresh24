package mart.fresh.com.service;


import org.springframework.data.domain.Page;
import jakarta.validation.Valid;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.util.OrderedProductCreatedEvent;
import reactor.core.publisher.Flux;


public interface OrderedProductService {
	Page<MyOrderedProductDto> getOrderedProductByMemberId(String memberId, int page, int size);
	Flux<MyOrderedProductDto> streamOrderedProductsByStoreId(String memberId);
	void handleOrderedProductCreatedEvent(OrderedProductCreatedEvent event);
	
	
	void createOrder(@Valid MyOrderedProductDto myOrderedProductDto);
}