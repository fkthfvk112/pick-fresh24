package mart.fresh.com.service;

import java.util.List;
import java.util.Map;

import mart.fresh.com.data.dto.ManagerOrderDto;
import mart.fresh.com.data.dto.ManagerOrderObjDto;
import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;

public interface ManagerOrderService {
	void saveManagerOrder( List<ManagerOrderDto> managerOrderDtoList);
	List<ManagerOrderWithObj> getManagerOrderByFilter(int filter);
	List<ManagerOrderObjDto> showMyOrder(int storeId);
}
