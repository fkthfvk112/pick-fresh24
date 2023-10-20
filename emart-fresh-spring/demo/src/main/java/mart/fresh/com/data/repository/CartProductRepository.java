package mart.fresh.com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Integer>{
    List<CartProduct> findByCartMemberMemberId(String memberId);

	int deleteByCartMemberMemberIdAndCartProductId(String memberId, int cartProductId);
	List<CartProduct> findByProduct_ProductTitle(String productName);

	@Query("SELECT cp "
			+ "FROM CartProduct cp "
			+ "JOIN FETCH cp.product "
			+ "WHERE cp.cart.cartId = :cartId ")
	List<CartProduct> findCartProductListByCartId(@Param("cartId") int cartId);

	CartProduct findByCart(Cart cart);

	void deleteByCart_CartId(int cartId);

	CartProduct findByCartCartIdAndProductProductId(int cartId, Integer productId);
}
