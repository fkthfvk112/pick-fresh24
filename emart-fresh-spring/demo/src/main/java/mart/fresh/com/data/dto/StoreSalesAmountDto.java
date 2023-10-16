package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class StoreSalesAmountDto {
	private int quarter;
    private Timestamp orderedDate;
    private int totalAmount;
}
