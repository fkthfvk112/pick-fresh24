package mart.fresh.com.service;


import java.util.List;

import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.entity.OrderedProduct;


public interface OrderedProductService {
	Page<MyOrderedProductDto> getOrderedProductByMemberId(String memberId, int page, int size);
	Page<MyOrderedProductDto> getOrderedListByStoreId(int storeId, int page, int size);
	void saveOrderedProduct(OrderedProduct orderedProduct);
	List<OrderedProduct> findByMemberMemberId(String memberId);
}