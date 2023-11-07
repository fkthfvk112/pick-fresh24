package mart.fresh.com.service.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.service.ProductSearchStrategy;
import mart.fresh.com.service.ProductService;

@Service
public class ProductServiceImpl implements ProductService{
	private final ProductDao productDao;
	private final ProductRepository productRepository;
	private final ModelMapper moddelMapper;

	@Autowired
	public ProductServiceImpl(ProductDao productDao, ProductRepository productRepository, ModelMapper moddelMapper) {
		this.productDao = productDao;
		this.productRepository = productRepository;
		this.moddelMapper = moddelMapper;
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
		//List<ProductDto> productDtoList = productDao.getProductDtoListByFilter(productFilterDto, offset, limit);
		GetSearchedProductDtoListByFilter context = new GetSearchedProductDtoListByFilter();

		switch (productFilterDto.getSelect()) {
		case 0: {
			ProductSearchStrategy strategy = new ProductSelectZeroStrategy(productRepository, moddelMapper);
			context.setFilterStrategy(strategy);
			return context.getProductListByFilter(productFilterDto, offset, limit);
		}
		case 1: {
			ProductSearchStrategy strategy = new ProductSelectOneStrategy(productRepository, moddelMapper);
			context.setFilterStrategy(strategy);
			return context.getProductListByFilter(productFilterDto, offset, limit);
		}
		case 2: {
			ProductSearchStrategy strategy = new ProductSelectTwoStrategy(productRepository, moddelMapper);
			context.setFilterStrategy(strategy);
			return context.getProductListByFilter(productFilterDto, offset, limit);
		}
		case 3: {
			ProductSearchStrategy strategy = new ProductSelectThreeStrategy(productRepository, productDao, moddelMapper);
			context.setFilterStrategy(strategy);
			return context.getProductListByFilter(productFilterDto, offset, limit);
		}
		default:
			throw new IllegalArgumentException("Unexpected select value: " + productFilterDto.getSelect());
		}
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
}

