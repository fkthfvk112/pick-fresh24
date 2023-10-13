package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class StoreSalesAmountDto {
    private Timestamp orderedDate;
    private int totalAmount;
}
