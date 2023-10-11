package mart.fresh.com.data.dao;

import java.util.List;
import java.util.Map;

import mart.fresh.com.data.dto.ManagerOrderDto;
import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;

public interface ManagerOrderDao {
	void saveManagerOrder(List<ManagerOrderWithId> managerOrderWithId);
	List<ManagerOrderWithObj>  getManagerOrderByFilter(int filter);
	List<ManagerOrderWithObj> showMyOrder(int storeId);
}