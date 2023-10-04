package mart.fresh.com.service;

import java.util.List;

import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.dto.StoreDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;

public interface ProductService {
	List<Store> getNearStoreInfos(String productName);
	List<Product> getProductsByStoreId(int storeId);
	void saveProduct(Product product);
	List<ProductDto> getAllProdct();
	List<ProductDto> getProductDtoListByFilter(ProductFilterDto productFilterDto, int offset, int limit);
	List<ProductDto> getStoreProductsDtoByFilter(ProductFilterDto productFilterDto, int offset, int limit);
	ProductDto getProductDetailByProductId(int productId);
	Product findByProductId(int productId);

//	ProductResponseDto getProduct(Long number);
//	ProductResponseDto saveProduct(ProductDto productDto);
//	ProductResponseDto changeProductName(Long number, String name) throws Exception;
//	void deleteProduct(Long number) throws Exception;
//	List<ProductResponseDto> findProductsByPriceGreaterThanEqual(int price);
}
