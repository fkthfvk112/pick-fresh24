package mart.fresh.com.data.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.StoreProductWithId;


public interface StoreProductRepository extends JpaRepository<StoreProductWithId, Integer> {

	StoreProductWithId findByStoreIdAndProductId(int storeId,  int productId);
}