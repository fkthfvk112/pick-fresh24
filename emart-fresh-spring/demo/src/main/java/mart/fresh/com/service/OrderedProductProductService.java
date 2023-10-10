package mart.fresh.com.service;

import java.util.List;

import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.entity.Product;

public interface OrderedProductProductService {

	void saveOrderedProductProduct(OrderedProductProduct orderedProductProduct);
	List<OrderedProductProduct> findByOrderedProductOrderedProductId(int orderedProductId);
	Product getProductDetails(int productId);
	OrderedProductProduct findByOrderedProductProductId(int orderedProductProductId);

}
