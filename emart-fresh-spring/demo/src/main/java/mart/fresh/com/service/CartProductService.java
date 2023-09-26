package mart.fresh.com.service;

public interface CartProductService {
    void removeProductFromCart(String memberId, int cartProductId);

	boolean updateCartProductQuantity(String memberId, int cartProductId, int cartProductQuantity);

}
