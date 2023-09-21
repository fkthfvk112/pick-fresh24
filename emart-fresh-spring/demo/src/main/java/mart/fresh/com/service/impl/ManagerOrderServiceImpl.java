package mart.fresh.com.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.ManagerOrderDao;
import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dto.ManagerOrderDto;
import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;
import mart.fresh.com.service.ManagerOrderService;

@Service
public class ManagerOrderServiceImpl implements ManagerOrderService {
	private final ManagerOrderDao managerOrderDao;

	@Autowired
	public ManagerOrderServiceImpl(ManagerOrderDao managerOrderDao) {
		this.managerOrderDao = managerOrderDao;
		
	}//엔티티 객체 DTO객체 전환
	@Override
	public void saveManagerOrder(List<ManagerOrderDto> managerOrderDtoList) {
		
		System.out.println("서비스층 " + managerOrderDtoList.toString() );
		
		List<ManagerOrderWithId> mEntityList = new ArrayList<>();
		for (ManagerOrderDto managerOrderDto : managerOrderDtoList) {
		    ManagerOrderWithId mEntity = new ManagerOrderWithId();
		    mEntity.setManagerOrderDate(managerOrderDto.getManagerOrderDate());
		    mEntity.setManagerOrderNum(managerOrderDto.getManagerOrderNum());
		    mEntity.setManagerOrderQuantity(managerOrderDto.getManagerOrderQuantity());
		    mEntity.setManagerOrderStatus(managerOrderDto.isManagerOrderStatus());
		    mEntity.setProductId(managerOrderDto.getProductId());
		    mEntity.setStoreId(managerOrderDto.getStoreId());
		    System.out.println(mEntity);

		    mEntityList.add(mEntity);
		}
		System.out.println("서비스층2 " + mEntityList.toString() );

		
		managerOrderDao.saveManagerOrder(mEntityList);
	}
	@Override
	public List<ManagerOrderWithObj> getManagerOrderByFilter(int filter) {
		return managerOrderDao.getManagerOrderByFilter(filter);
	}
	
}