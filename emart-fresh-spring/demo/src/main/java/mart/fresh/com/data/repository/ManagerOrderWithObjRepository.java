package mart.fresh.com.data.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;

public interface ManagerOrderWithObjRepository extends JpaRepository<ManagerOrderWithObj, Integer> {
	@Query("SELECT mo FROM ManagerOrderWithObj mo "
	        + "ORDER BY "
	        + "CASE WHEN :filter = 1 THEN mo.managerOrderDate END ASC, "
	        + "CASE WHEN :filter = 2 THEN mo.managerOrderDate END DESC, "
	        + "mo.store.storeId ASC")
	List<ManagerOrderWithObj> getManagerOrderByFilter(@Param("filter") int filter);
	
	@Query("SELECT mo FROM ManagerOrderWithObj mo "
	        + "JOIN mo.product p "
	        + "WHERE DATE(mo.managerOrderDate) = :date AND mo.managerOrderStatus = FALSE "
	        + "ORDER BY mo.store.storeId ASC")
	List<ManagerOrderWithObj> getManagerOrderByDate(@Param("date") Date searchingDate);

}
