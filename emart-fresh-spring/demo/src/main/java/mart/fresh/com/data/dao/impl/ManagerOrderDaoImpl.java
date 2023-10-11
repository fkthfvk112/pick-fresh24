package mart.fresh.com.data.dao.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.ManagerOrderDao;
import mart.fresh.com.data.dto.ManagerOrderDto;
import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.repository.ManagerOrderWithIdRepository;
import mart.fresh.com.data.repository.ManagerOrderWithObjRepository;
import mart.fresh.com.data.repository.ProductRepository;

@Component
public class ManagerOrderDaoImpl implements ManagerOrderDao{
	private final ManagerOrderWithIdRepository managerOrderWithIdRepository;
	private final ManagerOrderWithObjRepository managerOrderWithObjRepository;

	@Autowired
	public ManagerOrderDaoImpl(
			ManagerOrderWithId managerOrderWithId, 
			ManagerOrderWithIdRepository managerOrderWithIdRepository,
			ManagerOrderWithObjRepository managerOrderWithObjRepository) {
		this.managerOrderWithIdRepository = managerOrderWithIdRepository;
		this.managerOrderWithObjRepository = managerOrderWithObjRepository;
	}
	
	@Override
	public void saveManagerOrder(List<ManagerOrderWithId> managerOrderWithId) {//수정 : product 생성해서 값 넘겨주기
		System.out.println("---saveManagerOrder Dao" + managerOrderWithId.toString());
		managerOrderWithIdRepository.saveAll(managerOrderWithId);
	}

	@Override
	public List<ManagerOrderWithObj>  getManagerOrderByFilter(int filter) {
		List<ManagerOrderWithObj> orderList = managerOrderWithObjRepository.getManagerOrderByFilter(filter);
		System.out.println("---getManagerOrderByFilter dao" + orderList);
		return orderList;
	}

	@Override
	public List<ManagerOrderWithId> showMyOrder(int storeId) {
		List<ManagerOrderWithId> orderList = managerOrderWithIdRepository.findByStoreId(storeId);
		
		return orderList;
	}
}

