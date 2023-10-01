package mart.fresh.com.service.impl;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;

import mart.fresh.com.data.dao.OrderedProductDao;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.repository.MypageRepository;
import mart.fresh.com.data.repository.OrderedProductRepository;
import mart.fresh.com.service.OrderedProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import mart.fresh.com.util.EventPublisherHolder;
import mart.fresh.com.util.OrderedProductCreatedEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class OrderedProductServiceImpl implements OrderedProductService {
    // Logger 객체를 static final로 선언
    private static final Logger LOGGER = LoggerFactory.getLogger(OrderedProductServiceImpl.class);
    
    private final OrderedProductDao orderedProductDao;
    private final OrderedProductRepository orderedProductRepository;

    private FluxSink<MyOrderedProductDto> sink;
    private final Flux<MyOrderedProductDto> flux;
    

    @Autowired
    public OrderedProductServiceImpl(OrderedProductDao orderedProductDao,
                                    OrderedProductRepository orderedProductRepository) {
        this.orderedProductDao = orderedProductDao;
        this.orderedProductRepository = orderedProductRepository;

        this.flux = Flux.<MyOrderedProductDto>create(emitter ->  this.sink = emitter, FluxSink.OverflowStrategy.BUFFER)
                        .publish()
                        .autoConnect();
    }

    @Override
    public Flux<MyOrderedProductDto> streamOrderedProductsByStoreId(String memberId) {
        LOGGER.info("memberId: {}에 대해 streamOrderedProductsByStoreId 호출됨", memberId);
        List<MyOrderedProductDto> initialData = loadInitialData(memberId);

        System.out.println("streamOrderedProductsByStoreId initialData : " + initialData.toString() );
        
        return Flux.concat(Flux.fromIterable(initialData), flux);
    }

    private List<MyOrderedProductDto> loadInitialData(String memberId) {
        List<OrderedProduct> ops = orderedProductDao.findByMemberMemberId(memberId);
        System.out.println("loadInitialData loadInitialData ops : " + ops.toString());
        return ops.stream().map(this::convertEntityOpToDto).collect(Collectors.toList());
    }
    
    @Override
    @EventListener
    public void handleOrderedProductCreatedEvent(OrderedProductCreatedEvent event) {
        // 이벤트 발생 시 streamOrderedProductsByStoreId 메서드를 호출하도록 변경
    	
    	System.out.println("handleOrderedProductCreatedEvent : " +  event.getOrderedProduct().toString());
    	
	    OrderedProduct orderedProduct = event.getOrderedProduct();
	    System.out.println("handleOrderedProductCreatedEventhandleOrderedProductCreatedEvent orderedProduct : " + orderedProduct.toString());
	    	      
	    MyOrderedProductDto dto = convertEntityOpToDto(orderedProduct);
	
	    System.out.println("handleOrderedProductCreatedEventhandleOrderedProductCreatedEvent dto dto : " + dto.toString());
	      
	      // sink 값이 있으면 다음 flux 클라이언트 연결 때 dto 값 
	    if (sink != null) {
	          sink.next(dto);
	    }
	    	
//        streamOrderedProductsByStoreId(event.getOrderedProduct().getMember().getMemberId());
        
    }
    

    
    @Override
    public Page<MyOrderedProductDto> getOrderedProductByMemberId(String memberId, int page, int size) {
        Page<OrderedProductProduct> orderedList = orderedProductDao.getOrderedProductByMemberId(memberId, page, size);
        return orderedList.map(this::convertEntityOppToDto);
    }
    

	private MyOrderedProductDto convertEntityOppToDto(OrderedProductProduct orderedProductProduct) {
        MyOrderedProductDto myOrderedProductDto = new MyOrderedProductDto();

        SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date orderedDate = orderedProductProduct.getOrderedProduct().getOrderedDate();
        String formattedDate = outputDateFormat.format(orderedDate);

        myOrderedProductDto.setStoreName(orderedProductProduct.getOrderedProduct().getStore().getStoreName());
        myOrderedProductDto.setStoreId(orderedProductProduct.getOrderedProduct().getStore().getStoreId());
        myOrderedProductDto.setPickup(orderedProductProduct.getOrderedProduct().isPickup());
        myOrderedProductDto.setOrderedDate(orderedProductProduct.getOrderedProduct().getOrderedDate());
        myOrderedProductDto.setTotalAmount(orderedProductProduct.getOrderedProduct().getTotalAmount());
        myOrderedProductDto.setOrderedProductId(orderedProductProduct.getOrderedProduct().getOrderedProductId());
        myOrderedProductDto.setOrderCode(formattedDate + orderedProductProduct.getOrderedProduct().getMember().getMemberId());

        myOrderedProductDto.setProductTitle(orderedProductProduct.getProduct().getProductTitle());
        myOrderedProductDto.setProductId(orderedProductProduct.getProduct().getProductId());
        myOrderedProductDto.setProductImgUrl(orderedProductProduct.getProduct().getProductImgUrl());
        myOrderedProductDto.setOrderedQuantity(orderedProductProduct.getOrderedQuantity());

        int myOrderedCount = orderedProductRepository.myOrderedCount(orderedProductProduct.getOrderedProduct().getMember().getMemberId());
        myOrderedProductDto.setMyOrderedCount(myOrderedCount);

        return myOrderedProductDto;
    }

	private MyOrderedProductDto convertEntityOpToDto(OrderedProduct orderedProduct) {
        MyOrderedProductDto myOrderedProductDto = new MyOrderedProductDto();

        myOrderedProductDto.setMemberId(orderedProduct.getMember().getMemberId());
        myOrderedProductDto.setStoreName(orderedProduct.getStore().getStoreName());
        myOrderedProductDto.setStoreId(orderedProduct.getStore().getStoreId());
        myOrderedProductDto.setPickup(orderedProduct.isPickup());
        myOrderedProductDto.setOrderedDate(orderedProduct.getOrderedDate());
        myOrderedProductDto.setTotalAmount(orderedProduct.getTotalAmount());
        myOrderedProductDto.setOrderedProductId(orderedProduct.getOrderedProductId());
           
        return myOrderedProductDto;
    }




	@Override
	public void createOrder(MyOrderedProductDto myOrderedProductDto) {

		orderedProductDao.saveOrderedProduct(myOrderedProductDto);
	}
	
}
