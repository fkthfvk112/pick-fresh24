package mart.fresh.com.data.repository;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import mart.fresh.com.data.dto.CartInfoDto;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.StoreProduct;

public interface CartRepository extends JpaRepository<Cart, Integer>{
    List<Cart> findByMemberMemberId(String memberId);
    Cart findByMember(Member member);
    
    @Query("SELECT NEW mart.fresh.com.data.dto.CartInfoDto(c.cartId, cp.cartProductId, p.productId, p.productTitle, p.productImgUrl, p.priceNumber, p.productTimeSale, cp.cartProductQuantity, c.store.storeId, c.store.storeName) " +
    	       "FROM CartProduct cp " +
    	       "JOIN cp.cart c " +
    	       "JOIN cp.product p " + 
    	       "WHERE c.member.memberId = :memberId")
    	List<CartInfoDto> getCartInfoByMemberId(@Param("memberId") String memberId);
    
    
    Cart findByMember_MemberId(String memberId);

    //현재 날짜 기준으로 해당 가게에 해당 상품 재고가 존재하는지 확인
    @Query("SELECT sp "
    	    + "FROM StoreProduct sp "
    	    + "WHERE sp.store.storeId = :storeId AND "
    	    + "    sp.product.productTitle = :productName AND "
    	    + "    sp.storeProductStock >= 0 AND "
    	    + "    sp.product.productExpirationDate >= :now")
    	List<StoreProduct> getAvailableProducts(@Param("storeId") int storeId, @Param("productName") String productName, @Param("now") Timestamp now);

}
