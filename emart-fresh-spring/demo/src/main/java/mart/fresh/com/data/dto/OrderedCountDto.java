package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class OrderedCountDto {

	private String productTitle;
	private long orderedCount;

	public OrderedCountDto(String productTitle, long orderedCount) {
		this.productTitle = productTitle;
		this.orderedCount = orderedCount;
	}

}
