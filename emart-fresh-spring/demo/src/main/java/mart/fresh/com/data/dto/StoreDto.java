package mart.fresh.com.data.dto;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;
import mart.fresh.com.data.entity.Member;

@Data
public class StoreDto {
	private String storeName;
	private int storeId;
	private String storeAddress;
	private double storeLongitude;
	private double storeLatitude;
    private Member memberId;
}