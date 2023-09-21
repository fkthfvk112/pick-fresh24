package mart.fresh.com.data.dao;

import java.util.Date;
import java.util.List;

import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;

public interface AdminDao {
	public List<ManagerOrderWithObj> getManagerOrderObjsByDate(Date date);
	public int insertOrderIntoStoreProduct(List<Integer> ordernums);
}