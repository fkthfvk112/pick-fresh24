package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class ProductFilterDto {
	private int storeId;
	private String searchingTerm;
	private int eventNumber;//1 2 3
	private int select;//1 2 3
}
