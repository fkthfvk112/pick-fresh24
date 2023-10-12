package mart.fresh.com.data.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.entity.ManagerOrderWithId;
import mart.fresh.com.data.entity.ManagerOrderWithObj;

public interface ManagerOrderWithIdRepository extends JpaRepository<ManagerOrderWithId, Integer> {

	@Query("SELECT mo FROM ManagerOrderWithId mo "
	        + "WHERE DATE(mo.managerOrderDate) = :date "
	        + "ORDER BY mo.storeId ASC")
	List<ManagerOrderWithId> getManagerOrderByDate(@Param("date") Date searchingDate);
	
	ManagerOrderWithId findById(int id);
	
	@Query("SELECT mo FROM ManagerOrderWithId mo "
			+ "WHERE mo.storeId = :storeId")
	List<ManagerOrderWithId> findByStoreId(@Param("storeId") int storeId);
}