package mart.fresh.com.service;

import java.util.List;

import org.springframework.stereotype.Service;

import mart.fresh.com.data.dto.ManagerOrderObjDto;
import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;

public interface AdminService {
	 List<ManagerOrderObjDto> getManagerOrderObjsByDate(String date);
	 int insertOrderIntoStoreProduct(List<Integer> ordernums);
}
