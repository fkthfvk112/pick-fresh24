package mart.fresh.com.service.impl;


import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import mart.fresh.com.data.dao.OrderedProductDao;
import mart.fresh.com.data.dto.MyOrderedProductDto;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.OrderedProductProduct;
import mart.fresh.com.data.repository.OrderedProductProductRepository;
import mart.fresh.com.data.repository.OrderedProductRepository;
import mart.fresh.com.service.OrderedProductService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import mart.fresh.com.util.NaverTtsService;
import mart.fresh.com.util.OrderedProductCreatedEvent;


@Service
public class OrderedProductServiceImpl implements OrderedProductService {
 
//    private static final Logger LOGGER = LoggerFactory.getLogger(OrderedProductServiceImpl.class);

    private final OrderedProductDao orderedProductDao;
    private final OrderedProductRepository orderedProductRepository;
    private final OrderedProductProductRepository orderedProductProductRepository;

    private FluxSink<MyOrderedProductDto> sink;
    private final Flux<MyOrderedProductDto> flux;

    private NaverTtsService naverTtsService;

    @Autowired
    public OrderedProductServiceImpl(OrderedProductDao orderedProductDao,
                                    OrderedProductRepository orderedProductRepository,
                                    OrderedProductProductRepository orderedProductProductRepository,
                                    NaverTtsService naverTtsService) {
        this.orderedProductDao = orderedProductDao;
        this.orderedProductRepository = orderedProductRepository;
        this.orderedProductProductRepository = orderedProductProductRepository;
        this.naverTtsService = naverTtsService;
        
        this.flux = Flux.<MyOrderedProductDto>create(emitter ->  this.sink = emitter, FluxSink.OverflowStrategy.BUFFER)
                        .publish()
                        .autoConnect();
    }

    @Override
    public Flux<MyOrderedProductDto> streamOrderedProductsByStoreId(String memberId) {
        List<MyOrderedProductDto> initialData = loadInitialData(memberId);     
        return Flux.concat(Flux.fromIterable(initialData), flux);
    }

    private List<MyOrderedProductDto> loadInitialData(String memberId) {
    	
    	System.out.println("loadInitialData loadInitialData loadInitialData memberId : " + memberId);
    	
        List<OrderedProduct> ops = orderedProductDao.findByMemberMemberId(memberId);
        return ops.stream().map(this::convertEntityOpToDto).collect(Collectors.toList());
    }
    
    @Override
    @EventListener
//    @Transactional
    public void handleOrderedProductCreatedEvent(OrderedProductCreatedEvent event) {
    	    	
	    // sink 값이 있으면 다음 flux 클라이언트 연결 때 dto 값 
	    if (sink != null) {
	    	 byte[] audioData = naverTtsService.synthesizeSpeech("주문 왔숑");
	    	 String audioDataEncoded = Base64.getEncoder().encodeToString(audioData);
	         MyOrderedProductDto dto = convertEntityOpToDto(event.getOrderedProduct());
	    	 dto.setAudioData(audioDataEncoded);
	         sink.next(dto);
	    }
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

        int myOrderedCount = orderedProductProductRepository.countByOrderedProductOrderedProductId(orderedProductProduct.getOrderedProduct().getOrderedProductId());
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
		public void saveOrderedProduct(OrderedProduct orderedProduct) {
			orderedProductDao.saveOrderedProduct(orderedProduct);
		}

		@Override
		public List<OrderedProduct> findByMemberMemberId(String memberId) {
			return orderedProductDao.findByMemberMemberId(memberId);
		}

		@Override
		public void completePickup(int orderedProductId) {
			orderedProductDao.completePickup(orderedProductId);
		}
		
		public OrderedProduct findByOrderedProductId(int orderedProductId) {
			return orderedProductDao.findByOrderedProductId(orderedProductId);

		}
	
}

