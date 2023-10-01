package mart.fresh.com.data.dao.impl;


import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import jakarta.transaction.Transactional;
import mart.fresh.com.data.dao.OrderedProductDao;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.entity.Coupon;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.CouponRepository;
import mart.fresh.com.data.repository.MypageRepository;
import mart.fresh.com.data.repository.OrderedProductProductRepository;
import mart.fresh.com.data.repository.OrderedProductRepository;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.data.repository.StoreRepository;
import reactor.core.publisher.Flux;


@Component
public class OrderedProductDaoImpl implements OrderedProductDao {

private final OrderedProductProductRepository orderedProductProductRepository;
private final OrderedProductRepository orderedProductRepository;
private final StoreRepository storeRepository;
private final MypageRepository mypageRepository;
private final CouponRepository couponRepository;
private final ProductRepository productRepository;


	@Autowired
	public OrderedProductDaoImpl(OrderedProductProductRepository orderedProductProductRepository,
													OrderedProductRepository orderedProductRepository,
													MypageRepository mypageRepository,
													CouponRepository couponRepository,
													ProductRepository productRepository,
													StoreRepository storeRepository) {
		this.orderedProductProductRepository = orderedProductProductRepository;
		this.orderedProductRepository = orderedProductRepository;
		this.storeRepository = storeRepository;
		this.mypageRepository = mypageRepository;
		this.couponRepository = couponRepository;
		this.productRepository = productRepository;
	}

	@Override
	public Page<OrderedProductProduct> getOrderedProductByMemberId(String memberId, int page, int size) {
		System.out.println("OrderedProductDaoImpl getOrderedProductByorderedProductId");
		
		Pageable pageable = PageRequest.of(page, size);
		boolean orderedDel = false;
		
		System.out.println("OrderedProductDaoImpl OrderedProductDaoImpl " + "memberId : " + memberId + " page : " + page + " size : " + size + " pageable : " + pageable);
		
		Page<OrderedProductProduct> results = orderedProductRepository.getOrderedProductProductByMemberId(memberId, orderedDel, pageable);

		System.out.println("OrderedProductDaoImpl OrderedProductDaoImpl : " + results.toString());
		
	    return results;
	}

	
	@Override
	public Flux<OrderedProduct> getOrderedListByStoreId(String memberId) {
	    boolean isPickup = false;
	    
//		System.out.println("OrderedProductDaoImpl storeInfo memberId : " + memberId);
	    
		Store store = storeRepository.findByMemberMemberId(memberId);
		
//		System.out.println("OrderedProductDaoImpl storeInfo : ");
		
	    List<OrderedProduct> orderedProduct = orderedProductRepository.findByIsPickupAndStoreStoreId(isPickup, store.getStoreId());
	    
	    return Flux.fromIterable(orderedProduct);
	}

	@Override
	public OrderedProduct findByIsPickupAndMemberMemberId(String memberId) {
		boolean isPickup = false;
		
		OrderedProduct orderedProduct = orderedProductRepository.findByIsPickupAndMemberMemberId(isPickup, memberId);
		return orderedProduct;
	}

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

	
	
	@Transactional
	@Override
	public void saveOrderedProduct(MyOrderedProductDto myOrderedProductDto) {
		
	Date currentDate = new Date();
	Timestamp currentTime = new Timestamp(currentDate.getTime());
	
	Member member = mypageRepository.findMemberByMemberId(myOrderedProductDto.getMemberId());
	Store store = storeRepository.findStoreByStoreId(myOrderedProductDto.getStoreId());
		
	OrderedProduct orderedProduct = new OrderedProduct();
	
	if(myOrderedProductDto.getCouponId() > 0) {
		Coupon coupon = couponRepository.getCouponByCouponId(myOrderedProductDto.getCouponId());
		orderedProduct.setCoupon(coupon);
	}
		orderedProduct.setMember(member);
		orderedProduct.setStore(store);
		orderedProduct.setOrderedDate(currentTime);
		orderedProduct.setOrderedDel(false);
		orderedProduct.setPickup(false);
		orderedProduct.setTotalAmount(myOrderedProductDto.getTotalAmount());
		
	
		
		orderedProductRepository.save(orderedProduct);
		
//		System.out.println("saveOrderedProduct saveOrderedProduct orderedProduct : " + orderedProduct.toString());
//		
//		Product product = productRepository.findProductByProductId(myOrderedProductDto.getStoreId());
//		
//		OrderedProductProduct orderedProductProduct = new OrderedProductProduct();
//			
//		orderedProductProduct.setOrderedProduct(orderedProduct);
//		orderedProductProduct.setOrderedQuantity(myOrderedProductDto.getOrderedQuantity());
//		orderedProductProduct.setProduct(product);
//	
//		System.out.println("orderedProductRepository.save orderedProductRepository.save 123");
//		
//		orderedProductProductRepository.save(orderedProductProduct);
	}


	
//	@Override
//	public OrderedProductProduct getOrderedProductProductEntityByorderedProductId(int orderedProductId) {
//		
//		System.out.println("getOrderedProductProductEntityByorderedProductId orderedProductId : " + orderedProductId);
//		
//		OrderedProductProduct orderedProductProduct = orderedProductProductRepository.findByorderedProductId(orderedProductId);
//		return orderedProductProduct;
//	}



}