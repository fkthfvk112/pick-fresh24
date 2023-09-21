package mart.fresh.com.data.dao.impl;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.AdminDao;
import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;
import mart.fresh.com.data.entity.StoreProduct;
import mart.fresh.com.data.entity.StoreProductWithId;
import mart.fresh.com.data.repository.ManagerOrderWithIdRepository;
import mart.fresh.com.data.repository.ManagerOrderWithObjRepository;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.data.repository.StoreProductRepository;

@Component
public class AdminDaoImpl implements AdminDao {
	private final ManagerOrderWithIdRepository managerOrderWithIdRepository;
	private final ManagerOrderWithObjRepository managerOrderWithObjRepository;
	private final StoreProductRepository storeProductRepository;
	
	@Autowired
	public AdminDaoImpl(
			ManagerOrderWithId managerOrderWithId, 
			ManagerOrderWithIdRepository managerOrderWithIdRepository,
			ManagerOrderWithObjRepository managerOrderWithObjRepository,
			StoreProductRepository storeProductRepository) {
		this.managerOrderWithIdRepository = managerOrderWithIdRepository;
		this.managerOrderWithObjRepository = managerOrderWithObjRepository;
		this.storeProductRepository = storeProductRepository;
	}
	
	@Override
	public List<ManagerOrderWithObj> getManagerOrderObjsByDate(Date date) {
		List<ManagerOrderWithObj> dateSortingOrders =  managerOrderWithObjRepository.getManagerOrderByDate(date);

		System.out.println("-----managerOrderWithObjRepository" + dateSortingOrders);
		for(ManagerOrderWithObj data : dateSortingOrders) {
			System.out.println("이름" + data.getProduct().getProductTitle());
		}
		return dateSortingOrders;
	}

	@Override
	public int insertOrderIntoStoreProduct(List<Integer> ordernums) {// 수정
		System.out.println("-----insertOrderIntoStoreProduct");
		for(int ordernum:ordernums) {
			Optional<ManagerOrderWithId> orderOptional = Optional.ofNullable(managerOrderWithIdRepository.findById(ordernum));
			
			//주문 목록이 있음
			if (orderOptional.isPresent()) {
				ManagerOrderWithId mowi = orderOptional.get();
				
				int orderedProductId = mowi.getProductId();
				int orderingStoreId = mowi.getStoreId();
				//같은 프로덕트 ID와 같은 StoreID를 가지는 가게 상풍 재고 값이 있음
				StoreProductWithId sp = storeProductRepository.findByStoreIdAndProductId(orderingStoreId, orderedProductId);
				
				//이미 product id와 sotre id가 일치하는 테이블 존재
				if(sp!= null) {
					System.out.println("존재합니다요!!");
					int stock = sp.getStoreProductStock();
					int updatedStock = mowi.getManagerOrderQuantity() + stock;
					sp.setStoreProductStock(updatedStock);
					storeProductRepository.save(sp);//존재하면 업데이트
					
				}
				else {//존재 안하면 새로 생성
					StoreProductWithId spwi = new StoreProductWithId();
					spwi.setProductId(mowi.getProductId());
					spwi.setStoreId(mowi.getStoreId());
					spwi.setStoreProductStock(mowi.getManagerOrderQuantity());
					storeProductRepository.save(spwi);	
					System.out.println("-----spwi값 ......! " + spwi);
				}
				
				//처리 후 주문 목록 드랍
				managerOrderWithIdRepository.delete(mowi);
	        }
		}
		return 0;
	}

}
