package mart.fresh.com.data.dao.impl;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import mart.fresh.com.data.dao.OrderedProductDao;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.repository.OrderedProductProductRepository;
import mart.fresh.com.data.repository.OrderedProductRepository;


@Component
public class OrderedProductDaoImpl implements OrderedProductDao {

	private final OrderedProductRepository orderedProductRepository;
	private final OrderedProductProductRepository orderedProductProductRepository;

	@Autowired
	public OrderedProductDaoImpl(OrderedProductRepository orderedProductRepository, OrderedProductProductRepository orderedProductProductRepository) {
		this.orderedProductRepository = orderedProductRepository;
		this.orderedProductProductRepository = orderedProductProductRepository;
	}

	@Override
	public Page<OrderedProductProduct> getOrderedProductByMemberId(String memberId, int page, int size) {
		System.out.println("OrderedProductDaoImpl getOrderedProductByorderedProductId");
		
		Pageable pageable = PageRequest.of(page, size);
		boolean orderedDel = false;
		
		System.out.println("OrderedProductDaoImpl OrderedProductDaoImpl " + "memberId : " + memberId + " page : " + page + " size : " + size + " pageable : " + pageable);
		
		Page<OrderedProductProduct> results = orderedProductProductRepository.getOrderedProductProductByMemberId(memberId, orderedDel, pageable);

		System.out.println("OrderedProductDaoImpl OrderedProductDaoImpl : " + results.toString());
		
	    return results;
	}

	@Override
	public Page<OrderedProductProduct> getOrderedListByStoreId(int storeId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		boolean isPickup = false;
		

		Page<OrderedProductProduct> results = orderedProductProductRepository.getOrderedListByStoreId(storeId, isPickup, pageable);

	    return results;
	}
	
	@Override
	public void saveOrderedProduct(OrderedProduct orderedProduct) {
		orderedProductRepository.save(orderedProduct);
	}

	@Override
	public List<OrderedProduct> findByMemberMemberId(String memberId) {
		return orderedProductRepository.findByMemberMemberId(memberId);
	}
	
}