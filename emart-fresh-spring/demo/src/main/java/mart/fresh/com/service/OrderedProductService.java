package mart.fresh.com.service;


import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.MyOrderedProductDto;


public interface OrderedProductService {
	Page<MyOrderedProductDto> getOrderedProductByMemberId(String memberId, int page, int size);
	Page<MyOrderedProductDto> getOrderedListByStoreId(int storeId, int page, int size);
}