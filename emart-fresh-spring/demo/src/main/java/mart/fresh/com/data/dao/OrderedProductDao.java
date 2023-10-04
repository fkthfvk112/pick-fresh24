package mart.fresh.com.data.dao;

import java.util.List;

import org.springframework.data.domain.Page;

import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;


public interface OrderedProductDao {
	Page<OrderedProductProduct> getOrderedProductByMemberId(String memberId, int page, int size);
	Page<OrderedProductProduct> getOrderedListByStoreId(int storeId, int page, int size);
	void saveOrderedProduct(OrderedProduct orderedProduct);
	List<OrderedProduct> findByMemberMemberId(String memberId);
}