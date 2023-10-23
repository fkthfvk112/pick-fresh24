package mart.fresh.com.data.dto;

import java.sql.Timestamp;
import java.time.LocalDate;

import lombok.Data;

@Data
public class StoreSalesAmountDto {
	private int quarter;
    private LocalDate orderedDate;
    private int totalAmount;
}
