package mart.fresh.com.data.dao;

import java.util.List;

import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;

public interface ProductDao {
	List<Store> getNearStoreInfos(String productName); //수정 entity to dto
	List<Product> getProductsByStoreId(int storeId);
	List<Product> getAllProdct();
	Product saveProduct(Product product);
	List<ProductDto> getProductDtoListByFilter(ProductFilterDto productFilterDto, int offset, int limit);
	List<ProductDto> getStoreProductsByFilter(ProductFilterDto productFilterDto, int offset, int limit);
	Product getProductDetailByProductId(int productId);
	Product findByProductId(int productId);
}
