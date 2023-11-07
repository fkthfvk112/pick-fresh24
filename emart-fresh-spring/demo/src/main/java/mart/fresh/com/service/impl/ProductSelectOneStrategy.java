package mart.fresh.com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;

import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.service.ProductSearchStrategy;
import mart.fresh.com.util.ProductUtil;

public class ProductSelectOneStrategy implements ProductSearchStrategy{
	private final ProductRepository productRepository;
	private final ModelMapper modelMapper;

	public ProductSelectOneStrategy(ProductRepository productRepository, ModelMapper modelMapper) {
		this.productRepository = productRepository;
		this.modelMapper = modelMapper;
	}
		
	@Override
	public List<ProductDto> getFilteredProductList(ProductFilterDto productFilterDto, int offset, int limit) {
		List<Product> productEntityList = productRepository
				.getProductDtoListByFilter(
					productFilterDto.getSearchingTerm(),
					productFilterDto.getEventNumber(),
					productFilterDto.getSelect(),
					PageRequest.of(offset, limit));//페이징 offset limit
		
		/* 중복 이름 처리, 중복 이름이 존재하면 아예 보여주지 않도록 함*/
		List<ProductDto> uniqueTitleProductList = ProductUtil.convertToUniqueNameProductList(productEntityList);

		return uniqueTitleProductList;
	}
}
