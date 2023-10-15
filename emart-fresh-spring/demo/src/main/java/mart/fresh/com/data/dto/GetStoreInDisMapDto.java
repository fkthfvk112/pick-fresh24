package mart.fresh.com.data.dto;

import java.util.List;

import lombok.Data;

@Data
public class GetStoreInDisMapDto {
	 String productName;
     int maxDis;
     double userLatitude;
     double userLongitude;
};
