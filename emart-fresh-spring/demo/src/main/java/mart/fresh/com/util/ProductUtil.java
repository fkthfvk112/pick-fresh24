package mart.fresh.com.util;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;

import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductWithOrderCountDto;
import mart.fresh.com.data.entity.Product;

public class ProductUtil {
    public static List<ProductDto> convertToUniqueNameProductList(List<Product> productList) {
        ModelMapper modelMapper = new ModelMapper();

    	return productList.stream()
            .collect(Collectors.toMap(
                Product::getProductTitle,
                product -> modelMapper.map(product, ProductDto.class),
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ))
            .values()
            .stream()
            .collect(Collectors.toList());
    }

    public static List<ProductDto> convertToUniqueNameProductDtoList_order(List<ProductWithOrderCountDto> productListWithOrder) {
        ModelMapper modelMapper = new ModelMapper();

    	return productListWithOrder.stream()
            .collect(Collectors.toMap(
                ProductWithOrderCountDto::getProductTitle,
                dto -> modelMapper.map(dto, ProductDto.class),
                (existing, replacement) -> existing,
                LinkedHashMap::new
            ))
            .values()
            .stream()
            .collect(Collectors.toList());
    }
}
