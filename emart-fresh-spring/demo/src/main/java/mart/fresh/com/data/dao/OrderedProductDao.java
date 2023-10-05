package mart.fresh.com.data.dao;

import java.util.List;
import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import reactor.core.publisher.Flux;


public interface OrderedProductDao {
	Page<OrderedProductProduct> getOrderedProductByMemberId(String memberId, int page, int size);
	List<OrderedProduct> findByMemberMemberId(String memberId);
	void saveOrderedProduct(OrderedProduct orderedProduct);
	Flux<OrderedProduct> getOrderedListByStoreId(String memberId);
	OrderedProduct findByIsPickupAndMemberMemberId(String memberId);
	void completePickup(int orderedProductId);
}