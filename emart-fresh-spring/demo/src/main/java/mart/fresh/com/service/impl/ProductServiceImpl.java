package mart.fresh.com.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	private final ProductDao productDao;

	@Autowired
	public ProductServiceImpl(ProductDao productDao) {
		this.productDao = productDao;
	}//엔티티 객체 DTO객체 전환
	
	@Override
	public List<Store> getNearStoreInfos(String productName){
		List<Store> stores = productDao.getNearStoreInfos(productName);
		
		return stores;
	}

	@Override
	public List<Product> getProductsByStoreId(int storeId) {
		List<Product> Products = productDao.getProductsByStoreId(storeId);
		
		return Products;
	}

	@Override
	public void saveProduct(Product product) {
		productDao.saveProduct(product);
	}

	@Override
	public List<ProductDto> getAllProdct() {
		List<ProductDto> dtoList = new ArrayList<>();
		
		List<Product> productList = productDao.getAllProdct();
		if (productList != null && !productList.isEmpty()) {
			for (Product product : productList) {
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
		}
		else {
			System.out.println("product list의 값이 없음");
		}
			
		return dtoList;
	}

	@Override
	public List<ProductDto> getProductDtoListByFilter(ProductFilterDto productFilterDto, int offset, int limit) {
		 List<ProductDto> productDtoList = productDao.getProductDtoListByFilter(productFilterDto, offset, limit);
		
		return productDtoList;
	}

	@Override
	public List<ProductDto> getStoreProductsDtoByFilter(ProductFilterDto productFilterDto, int offset, int limit) {
		 List<ProductDto> productDtoList = productDao.getStoreProductsByFilter(productFilterDto, offset, limit);

		return productDtoList;
	}

	@Override
	public ProductDto getProductDetailByProductId(int productId) {
		Product product = productDao.getProductDetailByProductId(productId);
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
		
		return dto;
	}
	
	@Override
	public Product findByProductId(int productId) {
		return productDao.findByProductId(productId);
	}

	@Override
	public void saveAllProductList(List<Product> productList) {
		productDao.saveAllProductList(productList);
		
	}
	
	//전환 예제
//	@Override
//	public ProductResponseDto getProduct(Long number) {
//		Product product = productDao.selectProduct(number);//db to entity
//		
//		ProductResponseDto productResponseDto = new ProductResponseDto();//dto 생성
//		productResponseDto.setNumber(product.getNumber());//entity to dto
//		productResponseDto.setName(product.getName());
//		productResponseDto.setPrice(product.getPrice());
//		productResponseDto.setStock(product.getStock());
//		
//		return productResponseDto;
//	}

}

