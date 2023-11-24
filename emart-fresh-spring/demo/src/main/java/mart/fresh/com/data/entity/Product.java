package mart.fresh.com.data.entity;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Index;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product", indexes = @jakarta.persistence.Index(name = "inx_productTitle", columnList = "product_title"))
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private int productId;
    
    @Column(name = "price_number")
    private int priceNumber;
    
    @Column(name = "price_string")
    private String priceString;
    
    @Column(name = "product_title")
    private String productTitle;
    
    @Column(name = "product_expiration_date")
    private Timestamp productExpirationDate; 

    @Column(name = "product_type")
    private int productType;
    
    @Column(name = "product_img_url")
    private String productImgUrl;
    
    @Column(name = "product_event")
    private int productEvent;
    
    @Column(name = "created_at")
    private Timestamp createdAt;
    
    @Column(name = "product_time_sale")
    private int productTimeSale;

    @Builder
	public Product(int productId, int priceNumber, String priceString, String productTitle,
			Timestamp productExpirationDate, int productType, String productImgUrl, int productEvent,
			Timestamp createdAt, int productTimeSale) {
		super();
		this.productId = productId;
		this.priceNumber = priceNumber;
		this.priceString = priceString;
		this.productTitle = productTitle;
		this.productExpirationDate = productExpirationDate;
		this.productType = productType;
		this.productImgUrl = productImgUrl;
		this.productEvent = productEvent;
		this.createdAt = createdAt;
		this.productTimeSale = productTimeSale;
	}
    
    

    //양방향 매핑
//    @ToString.Exclude//ToString에서 제외, 제외 안하면 무한 호출 에러
//    @OneToMany(mappedBy = "product")//
//    private List<StoreProduct> storeProducts;
}