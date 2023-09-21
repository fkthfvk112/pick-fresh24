package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class ApplyEmailDto {
	private String memberId;
    private String memberName;
    private String memberEmail;
    private String storeName;
    private int storeId;
    private String storeAddress;
}