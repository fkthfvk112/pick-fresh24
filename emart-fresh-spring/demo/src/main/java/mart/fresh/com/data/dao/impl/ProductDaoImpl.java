package mart.fresh.com.data.dao.impl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import java.util.Collections;

import mart.fresh.com.data.dao.ProductDao;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductFilterDto;
import mart.fresh.com.data.dto.ProductWithOrderCountDto;
import mart.fresh.com.data.entity.Product;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.OrderedProductProductRepository;
import mart.fresh.com.data.repository.ProductRepository;
import mart.fresh.com.util.CustomPageable;

@Component
public class ProductDaoImpl implements ProductDao{
	
	private final ProductRepository productRepository;
	private final OrderedProductProductRepository orderedProductProductRepository;
	private final StoreDaoImpl storeDaoImpl;
	
	@Autowired
	public ProductDaoImpl(ProductRepository productRepository,
			OrderedProductProductRepository orderedProductProductRepository,
			StoreDaoImpl storeDaoImpl) {
		this.productRepository = productRepository;
		this.orderedProductProductRepository = orderedProductProductRepository;
		this.storeDaoImpl = storeDaoImpl;
	}
	
	@Override
	public List<Store> getNearStoreInfos(String productName) {
		List<Store> resultStores = productRepository.getNearStoreInfos(productName);
		
		return resultStores;
	}

	@Override
	public List<Product> getProductsByStoreId(int storeId) {
		List<Product> resultProducts = productRepository.getProductsByStoreId(storeId);
		
		return resultProducts;
	}

	@Override
	public Product saveProduct(Product product) {
		Product savedProduct = productRepository.save(product);
		System.out.println("----saveProduct dato " + savedProduct);
		return savedProduct;
	}

	@Override
	public List<Product> getAllProdct() {
		List<Product> productList = productRepository.findAll();
		System.out.println("----getAllProdct dao " + productList);
		return productList;
	}

	
	public void orderByOrderNumber(List<ProductWithOrderCountDto> productList) {
		Collections.sort(productList, (item1, item2) -> Integer.compare(item2.getOrderCount(), item1.getOrderCount()));
	}
	
	
	public int getOrderNumberByProductName(String productName) {
		int count = orderedProductProductRepository.countOrderByProductName(productName);
		
		return count;
	}
	
	public List<ProductDto> deleteStoreProduct_stockIsZero(List<ProductDto> productList, int storeId) {
		List<ProductDto> notZeroProductList = new ArrayList<>();
		
		for(ProductDto dto:productList) {
			if(storeDaoImpl.getStoreProductStock(storeId, dto.getProductTitle()) > 0) {
				notZeroProductList.add(dto);
			}
					
		}
		
		return notZeroProductList;
	}
	
	@Override
	public List<ProductDto> getProductDtoListByFilter(ProductFilterDto productFilterDto, int offset, int limit) {
		if(productFilterDto.getSelect() == 3) {
			List<Product> productEntityList = productRepository
					.getProductDtoListByFilterNotPagable(
						productFilterDto.getSearchingTerm(),
						productFilterDto.getEventNumber());
			
			List<ProductWithOrderCountDto> productWithOrderCount = new ArrayList<>();
			
			for(Product product: productEntityList) {
				int orderCount = getOrderNumberByProductName(product.getProductTitle());
				ProductWithOrderCountDto pod = new ProductWithOrderCountDto();
				pod.setProductId(product.getProductId());
				pod.setPriceNumber(product.getPriceNumber());
				pod.setPriceString(product.getPriceString());
				pod.setProductTitle(product.getProductTitle());
				pod.setProductExpirationDate(product.getProductExpirationDate());
				pod.setProductType(product.getProductType());
				pod.setProductImgUrl(product.getProductImgUrl());
				pod.setProductEvent(product.getProductEvent());
				pod.setCreatedAt(product.getCreatedAt());
				pod.setProductTimeSale(product.getProductTimeSale());
				pod.setOrderCount(orderCount);
				productWithOrderCount.add(pod);
			}
			orderByOrderNumber(productWithOrderCount);
			
			/* 중복 이름 처리, 중복 이름이 존재하면 아예 보여주지 않도록 함*/
			List<ProductDto> dtoList = new ArrayList();
			
			for(ProductWithOrderCountDto product:productWithOrderCount) {
				boolean isContain = false;
				
				for(ProductDto dto:dtoList) {
					String dtoProductName = dto.getProductTitle();
					String entityProductName = product.getProductTitle();
					if(dtoProductName.equals(entityProductName)) {
						isContain = true;
						break;
					}
				}
				if(isContain) continue;
				
				ProductDto dto = new ProductDto();
		        dto.setProductId(product.getProductId());
		        dto.setPriceNumber(product.getPriceNumber());
		        dto.setPriceString(product.getPriceString());
		        dto.setProductTitle(product.getProductTitle());
		        dto.setProductExpirationDate(product.getProductExpirationDate());
		        dto.setProductType(product.getProductType());
		        dto.setProductImgUrl(product.getProductImgUrl());
		        dto.setProductEvent(product.getProductEvent());
		        dto.setCreatedAt(product.getCreatedAt());
		        dto.setProductTimeSale(product.getProductTimeSale());
		        dtoList.add(dto);
			}
			
						
			return CustomPageable.sliceList(dtoList, offset, limit);
		}
		
		else {
			
			List<Product> productEntityList = productRepository
					.getProductDtoListByFilter(
						productFilterDto.getSearchingTerm(),
						productFilterDto.getEventNumber(),
						productFilterDto.getSelect(),
						PageRequest.of(offset, limit));//페이징 offset limit
			
			System.out.println(productEntityList);
			
			/* 중복 이름 처리, 중복 이름이 존재하면 아예 보여주지 않도록 함*/
			List<ProductDto> dtoList = new ArrayList();
			
			for(Product product:productEntityList) {
				boolean isContain = false;
				
				for(ProductDto dto:dtoList) {
					String dtoProductName = dto.getProductTitle();
					String entityProductName = product.getProductTitle();
					if(dtoProductName.equals(entityProductName)) {
						isContain = true;
						break;
					}
				}
				if(isContain) continue;
				
				ProductDto dto = new ProductDto();
		        dto.setProductId(product.getProductId());
		        dto.setPriceNumber(product.getPriceNumber());
		        dto.setPriceString(product.getPriceString());
		        dto.setProductTitle(product.getProductTitle());
		        dto.setProductExpirationDate(product.getProductExpirationDate());
		        dto.setProductType(product.getProductType());
		        dto.setProductImgUrl(product.getProductImgUrl());
		        dto.setProductEvent(product.getProductEvent());
		        dto.setCreatedAt(product.getCreatedAt());
		        dto.setProductTimeSale(product.getProductTimeSale());
		        dtoList.add(dto);
			}
			
			return dtoList;
		}
	}

	@Override
	public List<ProductDto> getStoreProductsByFilter(ProductFilterDto productFilterDto, int offset, int limit) {
		System.out.println("-------getStoreProductsByFilter id : " + productFilterDto.getStoreId());

		if(productFilterDto.getSelect() == 3) {
			List<Product> productEntityList = productRepository
					.getStoreProductDtoListByFilterNotPagable(
						productFilterDto.getSearchingTerm(),
						productFilterDto.getEventNumber(),
						productFilterDto.getStoreId());
			
			List<ProductWithOrderCountDto> productWithOrderCount = new ArrayList<>();
			
			for(Product product: productEntityList) {
				int orderCount = getOrderNumberByProductName(product.getProductTitle());
				System.out.println("오더 카운트" + orderCount);
				ProductWithOrderCountDto pod = new ProductWithOrderCountDto();
				pod.setProductId(product.getProductId());
				pod.setPriceNumber(product.getPriceNumber());
				pod.setPriceString(product.getPriceString());
				pod.setProductTitle(product.getProductTitle());
				pod.setProductExpirationDate(product.getProductExpirationDate());
				pod.setProductType(product.getProductType());
				pod.setProductImgUrl(product.getProductImgUrl());
				pod.setProductEvent(product.getProductEvent());
				pod.setCreatedAt(product.getCreatedAt());
				pod.setProductTimeSale(product.getProductTimeSale());
				pod.setOrderCount(orderCount);
				productWithOrderCount.add(pod);
			}
			orderByOrderNumber(productWithOrderCount);
			
			System.out.println("결과값 " + productWithOrderCount);

			/* 중복 이름 처리, 중복 이름이 존재하면 아예 보여주지 않도록 함*/
			List<ProductDto> dtoList = new ArrayList();
			
			for(ProductWithOrderCountDto product:productWithOrderCount) {
				boolean isContain = false;
				
				for(ProductDto dto:dtoList) {
					String dtoProductName = dto.getProductTitle();
					String entityProductName = product.getProductTitle();
					if(dtoProductName.equals(entityProductName)) {
						isContain = true;
						break;
					}
				}
				if(isContain) continue;
				
				ProductDto dto = new ProductDto();
		        dto.setProductId(product.getProductId());
		        dto.setPriceNumber(product.getPriceNumber());
		        dto.setPriceString(product.getPriceString());
		        dto.setProductTitle(product.getProductTitle());
		        dto.setProductExpirationDate(product.getProductExpirationDate());
		        dto.setProductType(product.getProductType());
		        dto.setProductImgUrl(product.getProductImgUrl());
		        dto.setProductEvent(product.getProductEvent());
		        dto.setCreatedAt(product.getCreatedAt());
		        dto.setProductTimeSale(product.getProductTimeSale());
		        dtoList.add(dto);
			}
			
			
			//재고가 0인 것을 제외함
			dtoList = deleteStoreProduct_stockIsZero(dtoList, productFilterDto.getStoreId());
			
			return CustomPageable.sliceList(dtoList, offset, limit);
		}
		else {
			List<Product> productEntityList = productRepository
					.getStoreProductDtoListByFilter(
						productFilterDto.getSearchingTerm(),
						productFilterDto.getEventNumber(),
						productFilterDto.getSelect(),
						productFilterDto.getStoreId(),
						PageRequest.of(offset, limit));//페이징 offset limit
			
			System.out.println(productEntityList);
			
			List<ProductDto> dtoList = new ArrayList();
			
			for(Product product:productEntityList) {
				boolean isContain = false;
				for(ProductDto dto:dtoList) {
					String dtoProductName = dto.getProductTitle();
					String entityProductName = product.getProductTitle();
					if(dtoProductName.equals(entityProductName)) {
						isContain = true;
						break;
					}
				}
				
				if(isContain) continue;
				ProductDto dto = new ProductDto();
		        dto.setProductId(product.getProductId());
		        dto.setPriceNumber(product.getPriceNumber());
		        dto.setPriceString(product.getPriceString());
		        dto.setProductTitle(product.getProductTitle());
		        dto.setProductExpirationDate(product.getProductExpirationDate());
		        dto.setProductType(product.getProductType());
		        dto.setProductImgUrl(product.getProductImgUrl());
		        dto.setProductEvent(product.getProductEvent());
		        dto.setCreatedAt(product.getCreatedAt());
		        dto.setProductTimeSale(product.getProductTimeSale());
		        dtoList.add(dto);
			}
			
			//재고가 0인 것을 제외
			dtoList = deleteStoreProduct_stockIsZero(dtoList, productFilterDto.getStoreId());

			return dtoList;
		}
	}

	@Override
	public Product getProductDetailByProductId(int productId) {
		Product product = productRepository.getById((long) productId);
		System.out.println("프로덕트 " + product);
		
		return product;
	}
	
	@Override
	public Product findByProductId(int productId) {
		return productRepository.findByProductId(productId);
	}

	@Override
	public void saveAllProductList(List<Product> productList) {
		productRepository.saveAll(productList);
		
	}

}

