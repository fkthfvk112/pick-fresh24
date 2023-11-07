package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class ProductFilterDto {
	private int storeId;
	private String searchingTerm;
	//1+1
	//2+1
	//2+2
	private int eventNumber;//1 2 3
	//0 오래된 순
	//1 가격 낮은 순
	//2 가격 높은 순
	//3 주문 많은 순
	private int select;//0 1 2
}
