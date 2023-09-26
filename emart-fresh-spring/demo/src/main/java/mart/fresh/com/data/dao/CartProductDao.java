package mart.fresh.com.data.dao;

public interface CartProductDao {
    void removeProductFromCart(String memberId, int cartProductId);

	boolean updateCartProductQuantity(String memberId, int cartProductId, int cartProductQuantity);

}
