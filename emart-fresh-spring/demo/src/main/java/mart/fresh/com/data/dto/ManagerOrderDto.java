package mart.fresh.com.data.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ManagerOrderDto {
	private int productId;
    private int storeId;
    private int managerOrderNum;
    private boolean managerOrderStatus;
    private int managerOrderQuantity;
    private Timestamp managerOrderDate;
}