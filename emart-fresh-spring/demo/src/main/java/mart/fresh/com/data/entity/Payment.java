package mart.fresh.com.data.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "payment")
public class Payment {
	@Id
    @Column(name = "receipt_id")
    private String receiptId;
	
	@Column(name = "order_id")
    private String orderId;

    @Column(name = "pg")
    private String pg;

    @Column(name = "method_origin")
    private String methodOrigin;

    @Column(name = "method")
    private String method;

    @Column(name = "status_locale")
    private String statusLocale;

    @Column(name = "receipt_url")
    private String receiptUrl;
    
    @Column(name = "ordered_product_id")
    private int orderedProductId;
}
