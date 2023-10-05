package mart.fresh.com.data.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.ProductRepository;

@Component
public class ProductDaoImpl implements ProductDao{
	
	private final ProductRepository productRepository;
	
	@Autowired
	public ProductDaoImpl(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}
	
	@Override
	public List<Store> getNearStoreInfos(String productName) {
		List<Store> resultStores = productRepository.getNearStoreInfos(productName);
		
		return resultStores;
	}

	@Override
	public List<Product> getProductsByStoreId(int storeId) {
		List<Product> resultProducts = productRepository.getProductsByStoreId(storeId);
		
		return resultProducts;
	}

	@Override
	public Product saveProduct(Product product) {
		Product savedProduct = productRepository.save(product);
		System.out.println("----saveProduct dato " + savedProduct);
		return null;
	}

	@Override
	public List<Product> getAllProdct() {
		List<Product> productList = productRepository.findAll();
		System.out.println("----getAllProdct dao " + productList);
		return productList;
	}

	@Override
	public List<ProductDto> getProductDtoListByFilter(ProductFilterDto productFilterDto, int offset, int limit) {
		System.out.println("-------getProductDtoListByFilter");

		System.out.println("aaa" + productFilterDto.getSearchingTerm());
		System.out.println("bbb" + productFilterDto.getEventNumber());
		System.out.println("ccc" + productFilterDto.getSelect());

		List<Product> productEntityList = productRepository
				.getProductDtoListByFilter(
					productFilterDto.getSearchingTerm(),
					productFilterDto.getEventNumber(),
					productFilterDto.getSelect(),
					PageRequest.of(offset, limit));//페이징 offset limit
		
		System.out.println(productEntityList);
		
		/* 중복 이름 처리, 중복 이름이 존재하면 아예 보여주지 않도록 함*/
		List<ProductDto> dtoList = new ArrayList();
		
		for(Product product:productEntityList) {
			boolean isContain = false;
			
			for(ProductDto dto:dtoList) {
				String dtoProductName = dto.getProductTitle();
				String entityProductName = product.getProductTitle();
				if(dtoProductName.equals(entityProductName)) {
					isContain = true;
					break;
				}
			}
			if(isContain) continue;
			
			ProductDto dto = new ProductDto();
	        dto.setProductId(product.getProductId());
	        dto.setPriceNumber(product.getPriceNumber());
	        dto.setPriceString(product.getPriceString());
	        dto.setProductTitle(product.getProductTitle());
	        dto.setProductExpirationDate(product.getProductExpirationDate());
	        dto.setProductType(product.getProductType());
	        dto.setProductImgUrl(product.getProductImgUrl());
	        dto.setProductEvent(product.getProductEvent());
	        dto.setCreatedAt(product.getCreatedAt());
	        dto.setProductTimeSale(product.getProductTimeSale());
	        dtoList.add(dto);
		}
		
		return dtoList;
	}

	@Override
	public List<ProductDto> getStoreProductsByFilter(ProductFilterDto productFilterDto, int offset, int limit) {
		System.out.println("-------getStoreProductsByFilter id : " + productFilterDto.getStoreId());

		List<Product> productEntityList = productRepository
				.getStoreProductDtoListByFilter(
					productFilterDto.getSearchingTerm(),
					productFilterDto.getEventNumber(),
					productFilterDto.getSelect(),
					productFilterDto.getStoreId(),
					PageRequest.of(offset, limit));//페이징 offset limit
		
		System.out.println(productEntityList);
		
		List<ProductDto> dtoList = new ArrayList();
		
		for(Product product:productEntityList) {
			boolean isContain = false;
			for(ProductDto dto:dtoList) {
				String dtoProductName = dto.getProductTitle();
				String entityProductName = product.getProductTitle();
				if(dtoProductName.equals(entityProductName)) {
					isContain = true;
					break;
				}
			}
			
			if(isContain) continue;
			ProductDto dto = new ProductDto();
	        dto.setProductId(product.getProductId());
	        dto.setPriceNumber(product.getPriceNumber());
	        dto.setPriceString(product.getPriceString());
	        dto.setProductTitle(product.getProductTitle());
	        dto.setProductExpirationDate(product.getProductExpirationDate());
	        dto.setProductType(product.getProductType());
	        dto.setProductImgUrl(product.getProductImgUrl());
	        dto.setProductEvent(product.getProductEvent());
	        dto.setCreatedAt(product.getCreatedAt());
	        dto.setProductTimeSale(product.getProductTimeSale());
	        dtoList.add(dto);
		}
		
		return dtoList;
	}

	@Override
	public Product getProductDetailByProductId(int productId) {
		Product product = productRepository.getById((long) productId);
		System.out.println("프로덕트 " + product);
		
		return product;
	}
	
	@Override
	public Product findByProductId(int productId) {
		return productRepository.findByProductId(productId);
	}

	@Override
	public void saveAllProductList(List<Product> productList) {
		productRepository.saveAll(productList);
		
	}

}

