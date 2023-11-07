package mart.fresh.com.service.impl;

import java.util.List;

import org.springframework.stereotype.Component;

import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.service.ProductSearchStrategy;

@Component
public class GetSearchedProductDtoListByFilter {
	private ProductSearchStrategy productSearchStrategy;
	
	public List<ProductDto> getProductListByFilter(ProductFilterDto productFilterDto, int offset, int limit){
		return productSearchStrategy.getFilteredProductList(productFilterDto, offset, limit);
	}
	
    public void setFilterStrategy(ProductSearchStrategy productSearchStrategy){
        this.productSearchStrategy = productSearchStrategy;
    }
}
