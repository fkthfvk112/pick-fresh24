package mart.fresh.com.data.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.Data;

@Data
public class OrderedInfoDto {
	private int orderedProductId;
    private int storeId;
    private String storeName;
    private int couponId;
    private int totalAmount;
    private boolean isPickup;
    private boolean orderedDel;
    private Timestamp orderedDate;
    private List<OrderedProductProductDto> orderedProductProduct;

	public OrderedInfoDto() {
	}
    
    

}
