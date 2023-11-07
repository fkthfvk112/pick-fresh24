package mart.fresh.com.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.dto.ProductWithOrderCountDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.service.ProductSearchStrategy;
import mart.fresh.com.util.CustomPageable;
import mart.fresh.com.util.ProductUtil;

public class ProductSelectThreeStrategy implements ProductSearchStrategy{
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;
	private final ProductDao productDao;

	public ProductSelectThreeStrategy(ProductRepository productRepository, ProductDao productDao, ModelMapper modelMapper) {
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
		this.productDao = productDao;
	}
	
	@Override
	public List<ProductDto> getFilteredProductList(ProductFilterDto productFilterDto, int offset, int limit) {
		List<Product> productEntityList = productRepository
				.getProductDtoListByFilter(
					productFilterDto.getSearchingTerm(),
					productFilterDto.getEventNumber(),
					productFilterDto.getSelect(),
					PageRequest.of(offset, limit));//페이징 offset limit
		
		//order count 추가
		List<ProductWithOrderCountDto> productWithOrderCount = new ArrayList<>();
		for(Product product: productEntityList) {
			int orderCount = productDao.getOrderNumberByProductName(product.getProductTitle());
			ProductWithOrderCountDto pod = modelMapper.map(product, ProductWithOrderCountDto.class);
			pod.setOrderCount(orderCount);
			productWithOrderCount.add(pod);
		}
		
		//정렬
		Collections.sort(productWithOrderCount, (pre, post) -> Integer.compare(post.getOrderCount(), pre.getOrderCount()));
		
		/* 중복 이름 처리, 중복 이름이 존재하면 아예 보여주지 않도록 함*/
		List<ProductDto> uniqueTitleProductList = ProductUtil.convertToUniqueNameProductDtoList_order(productWithOrderCount);
				
		return CustomPageable.sliceList(uniqueTitleProductList, offset, limit);
	}
}
