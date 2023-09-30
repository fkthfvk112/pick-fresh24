package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class StoreDtoWithId {
	private String storeName;
	private int storeId;
	private String storeAddress;
	private double storeLongitude;
	private double storeLatitude;
    private String memberId;
}
