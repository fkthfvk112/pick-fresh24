package mart.fresh.com.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.repository.OrderedProductProductRepository;
import mart.fresh.com.service.OrderedProductProductService;
import mart.fresh.com.service.ProductService;

@Service
public class OrderedProductProductServiceImpl implements OrderedProductProductService{
	
	private final OrderedProductProductRepository orderedProductProductRepository;
	
	@Autowired
    private ProductService productService;
	
	@Autowired
	public OrderedProductProductServiceImpl(OrderedProductProductRepository orderedProductProductRepository) {
		this.orderedProductProductRepository = orderedProductProductRepository;
	}

	@Override
	public void saveOrderedProductProduct(OrderedProductProduct orderedProductProduct) {
		orderedProductProductRepository.save(orderedProductProduct);
	}

	@Override
	public List<OrderedProductProduct> findByOrderedProductOrderedProductId(int orderedProductId) {
		return orderedProductProductRepository.findByOrderedProductOrderedProductId(orderedProductId);
	}

	@Override
    public Product getProductDetails(int productId) {
        return productService.findByProductId(productId);
    }

	
	
}
