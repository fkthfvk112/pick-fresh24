package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;


@Data
public class OrderedProductObjDto {
	private MypageDto member;
    private ProductDto product;
    private StoreDto store;
    private int orderedProductId;
    private int orderedQuantity;
    private boolean isPickup;
    private boolean orderedDel;
    private Timestamp orderedDate;
}

