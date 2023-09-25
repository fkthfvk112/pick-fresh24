package mart.fresh.com.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mart.fresh.com.data.entity.Store;

public interface StoreRepository extends JpaRepository<Store, Integer>{
	
}