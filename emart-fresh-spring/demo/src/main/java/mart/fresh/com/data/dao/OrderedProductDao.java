package mart.fresh.com.data.dao;

import org.springframework.data.domain.Page;
import mart.fresh.com.data.entity.OrderedProductProduct;


public interface OrderedProductDao {
	Page<OrderedProductProduct> getOrderedProductByMemberId(String memberId, int page, int size);
	Page<OrderedProductProduct> getOrderedListByStoreId(int storeId, int page, int size);
	
}