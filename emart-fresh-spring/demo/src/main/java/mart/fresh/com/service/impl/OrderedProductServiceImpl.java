package mart.fresh.com.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import mart.fresh.com.data.dao.OrderedProductDao;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.repository.OrderedProductRepository;
import mart.fresh.com.service.OrderedProductService;

@Service
public class OrderedProductServiceImpl implements OrderedProductService {

	private final OrderedProductDao orderedProductDao;
	private final OrderedProductRepository orderedProductRepository;

	
	@Autowired
	public OrderedProductServiceImpl(OrderedProductDao orderedProductDao, OrderedProductRepository orderedProductRepository) {
		this.orderedProductDao = orderedProductDao;
		this.orderedProductRepository = orderedProductRepository;

	}
	
 		@Override
	    public Page<MyOrderedProductDto> getOrderedProductByMemberId(String memberId, int page, int size) {	
 			

	        System.out.println("OrderedProductServiceImpl getOrderedProductByorderedProductId : " + memberId + page + size);

	        Page<OrderedProductProduct> orderedList = orderedProductDao.getOrderedProductByMemberId(memberId, page, size);
	        

	        return orderedList.map(this::convertEntityToDto);
 		  }

 	    private MyOrderedProductDto convertEntityToDto(OrderedProductProduct orderedProduct) {
 	        MyOrderedProductDto myOrderedProductDto = new MyOrderedProductDto();
 	        
 	        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyyMMddHHmm"); 
 	        Date orderedDate = orderedProduct.getOrderedProduct().getOrderedDate();
 	        String formattedDate = outputDateFormat.format(orderedDate);
 	        
 	        myOrderedProductDto.setStoreName(orderedProduct.getOrderedProduct().getStore().getStoreName());
 	        myOrderedProductDto.setStoreId(orderedProduct.getOrderedProduct().getStore().getStoreId());
 	        myOrderedProductDto.setPickup(orderedProduct.getOrderedProduct().isPickup());
 	        myOrderedProductDto.setOrderedDate(orderedProduct.getOrderedProduct().getOrderedDate());
 	        myOrderedProductDto.setTotalAmount(orderedProduct.getOrderedProduct().getTotalAmount());
 	        myOrderedProductDto.setOrderedProductId(orderedProduct.getOrderedProduct().getOrderedProductId()); 
 	        myOrderedProductDto.setOrderCode(formattedDate + orderedProduct.getOrderedProduct(). getMember().getMemberId());
 	        
 	        myOrderedProductDto.setProductTitle(orderedProduct.getProduct().getProductTitle());
 	        myOrderedProductDto.setProductId(orderedProduct.getProduct().getProductId());
 	        myOrderedProductDto.setProductImgUrl(orderedProduct.getProduct().getProductImgUrl());
 	        myOrderedProductDto.setOrderedQuantity(orderedProduct.getOrderedQuantity());

 	       
 	        int myOrderedCount = orderedProductRepository.myOrderedCount(orderedProduct.getOrderedProduct().getMember().getMemberId());
 	        myOrderedProductDto.setMyOrderedCount(myOrderedCount);
 	        
 	       return myOrderedProductDto;
 	    }

		@Override
		public Page<MyOrderedProductDto> getOrderedListByStoreId(int storeId, int page, int size) {
			 System.out.println("OrderedProductServiceImpl getOrderedListByStoreId");
			 
			 Page<OrderedProductProduct> orderedList = orderedProductDao.getOrderedListByStoreId(storeId, page, size);
		     return orderedList.map(this::convertEntityToDto);
		}
		
		@Override
		public void saveOrderedProduct(OrderedProduct orderedProduct) {
			orderedProductDao.saveOrderedProduct(orderedProduct);
		}

		@Override
		public List<OrderedProduct> findByMemberMemberId(String memberId) {
			return orderedProductDao.findByMemberMemberId(memberId);
		}
	
}