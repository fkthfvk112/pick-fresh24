package mart.fresh.com.data.repository;


import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import mart.fresh.com.data.entity.OrderedProductProduct;



public interface OrderedProductProductRepository extends JpaRepository<OrderedProductProduct, Integer> {

	List<OrderedProductProduct> findByOrderedProductOrderedProductId(int orderedProductId);

}
 