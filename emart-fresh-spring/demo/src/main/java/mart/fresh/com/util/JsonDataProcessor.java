package mart.fresh.com.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import mart.fresh.com.data.entity.Product;
import mart.fresh.com.service.ProductService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Component

public class JsonDataProcessor {
	@Autowired
	private ProductService productService;

	public boolean processJsonData() {
		ObjectMapper objectMapper = new ObjectMapper();

		try {
			List<Product> productList = objectMapper.readValue(new File("Emart24_freshProduct.json"),
					new TypeReference<List<Product>>() {
					});

			productService.saveAllProductList(productList);

			System.out.println("=======ProductList saved successfully=======");
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
