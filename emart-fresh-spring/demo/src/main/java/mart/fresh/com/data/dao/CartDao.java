package mart.fresh.com.data.dao;

import java.util.List;

import mart.fresh.com.data.dto.CartInfoDto;

public interface CartDao {
    List<CartInfoDto> getCartInfo(String memberId);
    String addToCart(String memberId, String productName, int storeId, int requestQuantity);
}
