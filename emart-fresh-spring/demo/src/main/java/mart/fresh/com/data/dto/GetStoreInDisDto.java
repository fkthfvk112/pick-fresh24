package mart.fresh.com.data.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetStoreInDisDto {
	 List<String> productNames; //실제 데이터로 수정
     int maxDis;
     double userLatitude;
     double userLongitude;
};
