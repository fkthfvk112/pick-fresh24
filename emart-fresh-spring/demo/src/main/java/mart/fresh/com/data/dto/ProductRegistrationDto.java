package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class ProductRegistrationDto {
    private String priceNumber;
    private String productTitle;
    private String productType;
    private MultipartFile productImg;
}
