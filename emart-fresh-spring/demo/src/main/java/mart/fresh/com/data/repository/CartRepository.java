package mart.fresh.com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.entity.Cart;

public interface CartRepository extends JpaRepository<Cart, Integer>{
    List<Cart> findByMemberMemberId(String memberId);

    @Query("SELECT NEW mart.fresh.com.data.dto.CartInfoDto(c.cartId, cp.cartProductId, p.productId, p.productTitle, p.priceNumber, cp.cartProductQuantity, c.store.storeId) " +
    	       "FROM CartProduct cp " +
    	       "JOIN cp.cart c " +
    	       "JOIN cp.product p " +
    	       "WHERE c.member.memberId = :memberId")
    	List<CartInfoDto> getCartInfoByMemberId(@Param("memberId") String memberId);


}
