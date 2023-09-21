package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class ManagerOrderObjDto {
	private ProductDto product;
    private StoreDto store;
    private int managerOrderNum;
    private boolean managerOrderStatus;
    private int managerOrderQuantity;
    private Timestamp managerOrderDate;
}
