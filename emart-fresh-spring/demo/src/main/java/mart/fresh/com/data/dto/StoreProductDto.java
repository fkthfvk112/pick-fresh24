package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class StoreProductDto {

    private int storeProductId;
    private int storeProductStock;
    private boolean storeProductStatus;
    private ProductDto product;
    private StoreDto store;
}