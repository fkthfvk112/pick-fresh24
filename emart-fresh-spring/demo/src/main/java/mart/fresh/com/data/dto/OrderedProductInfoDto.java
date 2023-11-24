package mart.fresh.com.data.dto;

import java.sql.Timestamp;
import java.util.List;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrderedProductInfoDto {
	private String memberId;
    private int orderedProductId;
    private int storeId;
    private int couponId;
    private int totalAmount;
    private boolean isPickup;
    private boolean orderedDel;
    private Timestamp orderedDate;
    private List<OrderedProductProductDto> orderedProductProduct;
    
    @Builder
	public OrderedProductInfoDto(String memberId, int orderedProductId, int storeId, int couponId, int totalAmount,
			boolean isPickup, boolean orderedDel, Timestamp orderedDate,
			List<OrderedProductProductDto> orderedProductProduct) {
		super();
		this.memberId = memberId;
		this.orderedProductId = orderedProductId;
		this.storeId = storeId;
		this.couponId = couponId;
		this.totalAmount = totalAmount;
		this.isPickup = isPickup;
		this.orderedDel = orderedDel;
		this.orderedDate = orderedDate;
		this.orderedProductProduct = orderedProductProduct;
	}

}
