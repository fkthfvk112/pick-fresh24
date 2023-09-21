package mart.fresh.com.data.dto;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ProductDto {
    private int productId;
    private int priceNumber;
    private String priceString;
    private String productTitle;
    private Timestamp productExpirationDate;
    private int productType;
    private String productImgUrl;
    private int productEvent;
    private Timestamp createdAt;
    private int productTimeSale;
}