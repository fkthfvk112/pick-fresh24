package mart.fresh.com.data.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "store_product")
public class StoreProduct {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment strategy
    @Column(name = "store_product_id")
    private int storeProductId;
	 
    @Column(name = "store_product_stock")
    private int storeProductStock;

   
    @ManyToOne
    @JoinColumn(name="product_id")
    private Product product; 
    
    @ManyToOne
	@ToString.Exclude//ToString에서 제외, 제외 안하면 무한 호출 에러
    @JoinColumn(name="store_id")
    private Store store;

    @Builder
	public StoreProduct(int storeProductId, int storeProductStock, Product product, Store store) {
		super();
		this.storeProductId = storeProductId;
		this.storeProductStock = storeProductStock;
		this.product = product;
		this.store = store;
	} 
}