package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class GetStoreRequestDto {
	private double userLatitude;
    private double userLongitude;
    private int n;	// n km 반경
    private String partOfStoreName;	// 찾으려는 가게의 부분적 이름
}