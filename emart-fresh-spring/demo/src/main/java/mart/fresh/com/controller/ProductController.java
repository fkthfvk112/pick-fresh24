package mart.fresh.com.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.service.ProductService;


@RequestMapping("/product")
@RestController
public class ProductController {
	private final ProductService productService;
	
	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}
	
	@GetMapping()
	public List<Store> getNearStoreInfos(String productName) {
		System.out.println("-----getNearStoreInfos");
		
		List<Store> stores = productService.getNearStoreInfos(productName);
		System.out.println(stores);
	
		return stores;
	}
	
	@GetMapping("/product-list")
	public List<Product> getProductsByStoreId(int storeId) {
		System.out.println("-----getProductsByStoreId");
		
		List<Product> products = productService.getProductsByStoreId(storeId);
		System.out.println(products);
	
		return products;
	}
	
	
	//수정 : 안쓰는지 확인하고 삭제
	@GetMapping("/all-product-list")
	public List<ProductDto> getAllProducts() {
		System.out.println("-----getAllProducts");
		
		List<ProductDto> productDtoList = productService.getAllProdct();
		System.out.println("ProductDtoList" + productDtoList);
	
		return productDtoList;
	}
	
	@GetMapping("/all-product-list/filter")
	public List<ProductDto> getAllProductsByFilter(
			String searchingTerm, 
			@RequestParam(defaultValue = "0") int eventNumber, 
			@RequestParam(defaultValue = "0") int select,
			@RequestParam(defaultValue = "0")int offset,
			@RequestParam(defaultValue = "30")int limit
			){
		
		ProductFilterDto filterDto = new ProductFilterDto();
		filterDto.setSearchingTerm(searchingTerm);
		filterDto.setEventNumber(eventNumber);
		filterDto.setSelect(select);
		
		List<ProductDto> productDtoList =  productService.getProductDtoListByFilter(filterDto, offset, limit);
		System.out.println("먼트롤러" + productDtoList);
		
		return productDtoList; //수정 : 구현하기
	}
	
	@GetMapping("/store-product-list/filter")
	public List<ProductDto> getStoreProductsByFilter(
			String searchingTerm, 
			@RequestParam(defaultValue = "0") int eventNumber, 
			@RequestParam(defaultValue = "0") int select,
			@RequestParam(defaultValue = "0")int offset,
			@RequestParam(defaultValue = "30")int limit,
			int storeId
			){
		
		System.out.println("-----getStoreProductsByFilter");
		System.out.println("스토어 ID" + storeId);

		ProductFilterDto filterDto = new ProductFilterDto();
		filterDto.setSearchingTerm(searchingTerm);
		filterDto.setEventNumber(eventNumber);
		filterDto.setSelect(select);
		filterDto.setStoreId(storeId);
		
		List<ProductDto> productDtoList =  productService.getStoreProductsDtoByFilter(filterDto, offset, limit);
		System.out.println("스토어 값" + productDtoList);
		
		return productDtoList; //수정 : 구현하기
	}
	
	@GetMapping("/product-detail")
	public ProductDto getProductDetailByProductId(int productId) {
		
		ProductDto productDto = productService.getProductDetailByProductId(productId);
		return productDto;
		
	}

	@PostMapping("/add-product")
	public void insertProduct(@RequestBody Product product) {
		System.out.println("-----insertProduct");
		
		productService.saveProduct(product);
	}
}
