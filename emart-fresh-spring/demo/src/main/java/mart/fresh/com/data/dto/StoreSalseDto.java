package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class StoreSalseDto {
    private int storeId;
    private Timestamp orderedDate;
    private int totalAmount;
}
