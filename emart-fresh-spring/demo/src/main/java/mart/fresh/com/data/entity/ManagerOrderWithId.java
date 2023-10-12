package mart.fresh.com.data.entity;

import java.sql.Timestamp;
import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Component
@Table(name = "manager_order")
public class ManagerOrderWithId {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "manager_order_num")
	protected int managerOrderNum;
    
    @Column(name = "manager_order_status")
    protected boolean managerOrderStatus;
    
    @Column(name = "manager_order_quantity")
    protected int managerOrderQuantity;
    
    @Column(name = "manager_order_date")
    protected Timestamp managerOrderDate;
    
	@Column(name="store_id")
    private int storeId;
    
	@Column(name="product_id")
	private int productId;
}