package mart.fresh.com.data.dto;

import java.util.Map;

import lombok.Data;

@Data
public class ProductProcessResult {
	private String result;
    private Map<Integer, Integer> productInfoMap;
    
    public ProductProcessResult(String result, Map<Integer, Integer> productInfoMap) {
    	this.result = result;
        this.productInfoMap = productInfoMap;
	}
}
