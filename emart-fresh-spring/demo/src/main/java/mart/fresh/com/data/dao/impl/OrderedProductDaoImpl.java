package mart.fresh.com.data.dao.impl;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import mart.fresh.com.data.dao.OrderedProductDao;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.OrderedProductProductRepository;
import mart.fresh.com.data.repository.OrderedProductRepository;
import mart.fresh.com.data.repository.StoreRepository;
import reactor.core.publisher.Flux;



@Component
public class OrderedProductDaoImpl implements OrderedProductDao {

private final OrderedProductRepository orderedProductRepository;
private final OrderedProductProductRepository orderedProductProductRepository;
private final StoreRepository storeRepository;


	@Autowired
	public OrderedProductDaoImpl(OrderedProductRepository orderedProductRepository,
					OrderedProductProductRepository orderedProductProductRepository,
									StoreRepository storeRepository) {
		this.orderedProductRepository = orderedProductRepository;
		this.orderedProductProductRepository = orderedProductProductRepository;
		this.storeRepository = storeRepository;
	}

	@Override
	public Page<OrderedProductProduct> getOrderedProductByMemberId(String memberId, int page, int size) {
		System.out.println("OrderedProductDaoImpl getOrderedProductByorderedProductId");
		
		Pageable pageable = PageRequest.of(page, size);
		boolean orderedDel = false;
		
		System.out.println("OrderedProductDaoImpl OrderedProductDaoImpl " + "memberId : " + memberId + " page : " + page + " size : " + size + " pageable : " + pageable);
		
		Page<OrderedProductProduct> results = orderedProductRepository.getOrderedProductProductByMemberId(memberId, orderedDel, pageable);
		Set<Integer> uniqueOrderedProductIds = new HashSet<>();
	    List<OrderedProductProduct> uniqueResults = new ArrayList<>();

	    for(OrderedProductProduct result : results.getContent()) {
	        int orderedProductId = result.getOrderedProduct().getOrderedProductId();
	        if(!uniqueOrderedProductIds.contains(orderedProductId)) {
	            uniqueOrderedProductIds.add(orderedProductId);
	            uniqueResults.add(result);
	        }
	    }
	    long totalPages = orderedProductProductRepository.countUniqueOrderedProductsByMemberId(memberId);
	    
	    Page<OrderedProductProduct> orderedProductProduct = new PageImpl<>(uniqueResults, pageable, totalPages);
 
	    return orderedProductProduct;
	}

	
	@Override
	public Flux<OrderedProduct> getOrderedListByStoreId(String memberId) {
	    boolean isPickup = false;
	    
		Store store = storeRepository.findByMemberMemberId(memberId);

	    List<OrderedProduct> orderedProduct = orderedProductRepository.findByIsPickupAndStoreStoreId(isPickup, store.getStoreId());
	    
	    return Flux.fromIterable(orderedProduct);
	}

//	@Override
//	public OrderedProduct findByIsPickupAndMemberMemberId(String memberId) {
//		boolean isPickup = false;
//		
//		OrderedProduct orderedProduct = orderedProductRepository.findByIsPickupAndMemberMemberId(isPickup, memberId);
//		return orderedProduct;
//	}

	@Override
	public List<OrderedProduct> findByMemberMemberId(String memberId) {

		System.out.println("getOrderedProductProductEntitiesByMemberId memberId : " + memberId);
		
		Store store = storeRepository.findByMemberMemberId(memberId);
		boolean isPickup = false;
		 
		System.out.println("getOrderedProductProductEntitiesByMemberId store : " + store.toString());
		
		List<OrderedProduct> orderedProduct  = orderedProductRepository.findByIsPickupAndStoreStoreId(isPickup, store.getStoreId());
		System.out.println("getOrderedProductProductEntitiesByMemberId : " + orderedProduct.toString() );
		return orderedProduct;
	}


	@Override
	public void saveOrderedProduct(OrderedProduct orderedProduct) {
		orderedProductRepository.save(orderedProduct);
	}

	@Override

	public void completePickup(int orderedProductId) {

	    Optional<OrderedProduct> optionalOrderedProduct = orderedProductRepository.findById(orderedProductId);

	    if (optionalOrderedProduct.isPresent()) {
	        OrderedProduct orderedProduct = optionalOrderedProduct.get();

	        orderedProduct.setPickup(true);
	        orderedProductRepository.save(orderedProduct);
	    } 
	}

	public OrderedProduct findByOrderedProductId(int orderedProductId) {
		return orderedProductRepository.findByOrderedProductId(orderedProductId);
	}


}
