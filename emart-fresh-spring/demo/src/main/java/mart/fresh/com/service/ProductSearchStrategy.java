package mart.fresh.com.service;

import java.util.List;

import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;

public interface ProductSearchStrategy {
	List<ProductDto> getFilteredProductList(ProductFilterDto productFilterDto, int offset, int limit);
}
