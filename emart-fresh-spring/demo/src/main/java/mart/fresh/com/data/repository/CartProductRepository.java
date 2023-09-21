package mart.fresh.com.data.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import mart.fresh.com.data.entity.CartProduct;

public interface CartProductRepository extends JpaRepository<CartProduct, Integer>{
    List<CartProduct> findByCartMemberMemberId(String memberId);

	void deleteByCartMemberMemberIdAndCartProductId(String memberId, int cartProductId);
}
