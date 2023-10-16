package mart.fresh.com.data.dao.impl;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
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
			OrderedProductProductRepository orderedProductProductRepository, StoreRepository storeRepository) {
		this.orderedProductRepository = orderedProductRepository;
		this.orderedProductProductRepository = orderedProductProductRepository;
		this.storeRepository = storeRepository;
	}

	@Override
	public Page<OrderedProductProduct> getOrderedProductByMemberId(String memberId, int page, int size) {
		System.out.println("OrderedProductDaoImpl getOrderedProductByorderedProductId");

		Pageable pageable = PageRequest.of(page, size);
		boolean orderedDel = false;

		System.out.println("OrderedProductDaoImpl OrderedProductDaoImpl " + "memberId : " + memberId + " page : " + page
				+ " size : " + size + " pageable : " + pageable);

		Page<OrderedProductProduct> results = orderedProductRepository.getOrderedProductProductByMemberId(memberId,
				orderedDel, pageable);
		Set<Integer> uniqueOrderedProductIds = new HashSet<>();
		List<OrderedProductProduct> uniqueResults = new ArrayList<>();

		for (OrderedProductProduct result : results.getContent()) {
			int orderedProductId = result.getOrderedProduct().getOrderedProductId();
			if (!uniqueOrderedProductIds.contains(orderedProductId)) {
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

		List<OrderedProduct> orderedProduct = orderedProductRepository.findByIsPickupAndStoreStoreId(isPickup,
				store.getStoreId());

		return Flux.fromIterable(orderedProduct);
	}

	@Override
	public List<OrderedProduct> findByMemberMemberId(String memberId) {

		System.out.println("getOrderedProductProductEntitiesByMemberId memberId : " + memberId);

		Store store = storeRepository.findByMemberMemberId(memberId);
		boolean isPickup = false;

		System.out.println("getOrderedProductProductEntitiesByMemberId store : " + store.toString());

		List<OrderedProduct> orderedProduct = orderedProductRepository.findByIsPickupAndStoreStoreId(isPickup,
				store.getStoreId());
		System.out.println("getOrderedProductProductEntitiesByMemberId : " + orderedProduct.toString());
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

	
	
	private class DateRangeService {
	    
		private Timestamp[] calculateDateRange(Timestamp searchDate, String period) {
	        Calendar cal = Calendar.getInstance();
	        cal.setTime(searchDate);
	        
	        Timestamp startDate, endDate;
	        
	        switch (period) {
	            case "주간":
	                cal.add(Calendar.DAY_OF_MONTH, -6); // 6일을 뺌. 현재 날짜를 포함하여 7일 간의 데이터를 가져옴.
	                startDate = new Timestamp(cal.getTimeInMillis());
	                endDate = searchDate;
	                break;
	            case "월간":
	                cal.set(Calendar.DAY_OF_MONTH, 1); // 해당 월의 첫 날로 설정
	                startDate = new Timestamp(cal.getTimeInMillis());
	                cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH)); // 해당 월의 마지막 날로 설정
	                endDate = new Timestamp(cal.getTimeInMillis());
	                break;
	            case "연간":
	                cal.set(Calendar.DAY_OF_YEAR, 1); // 해당 연도의 첫 날로 설정
	                startDate = new Timestamp(cal.getTimeInMillis());
	                cal.set(Calendar.DAY_OF_YEAR, cal.getActualMaximum(Calendar.DAY_OF_YEAR)); // 해당 연도의 마지막 날로 설정
	                endDate = new Timestamp(cal.getTimeInMillis());
	                break;
	            default:
	                throw new IllegalArgumentException("Invalid period value");
	        }
	        
	        return new Timestamp[]{startDate, endDate};
	    }
	}
	
	
	@Override
	public List<OrderedProduct> findByStoreStoreIdAndOrderedDateBetween(int storeId, Timestamp searchDate, String period) {
	    DateRangeService dateRangeService = new DateRangeService();
	    Timestamp[] dateRange = dateRangeService.calculateDateRange(searchDate, period);
	    System.out.println("OrderedProductDaoImpl findByStoreStoreIdAndOrderedDateBetween : " + orderedProductRepository.findByStoreStoreIdAndOrderedDateBetweenOrderByOrderedDateAsc(storeId, dateRange[0], dateRange[1]));
	    
	    return orderedProductRepository.findByStoreStoreIdAndOrderedDateBetweenOrderByOrderedDateAsc(storeId, dateRange[0], dateRange[1]);
	}

	@Override
	public List<OrderedProductProduct> findByOrderedProductStoreStoreIdAndOrderedProductOrderedDateBetweenOrderByOrderedDateAsc(int storeId, Timestamp searchDate, String period) {
	    DateRangeService dateRangeService = new DateRangeService();
	    Timestamp[] dateRange = dateRangeService.calculateDateRange(searchDate, period);
	    System.out.println("OrderedProductDaoImpl findByOrderedProductStoreStoreIdAndOrderedProductOrderedDateBetweenOrderByOrderedDateAsc : " + orderedProductProductRepository.findByOrderedProductStoreStoreIdAndOrderedProductOrderedDateBetweenOrderByOrderedProductOrderedDateAsc(storeId, dateRange[0], dateRange[1]));
	    
	    return orderedProductProductRepository.findByOrderedProductStoreStoreIdAndOrderedProductOrderedDateBetweenOrderByOrderedProductOrderedDateAsc(storeId, dateRange[0], dateRange[1]);
	}

	
	
	
}
