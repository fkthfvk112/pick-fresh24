package mart.fresh.com.data.entity;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.ToString;

@Data
@Entity
@Table(name = "manager_order")
public class ManagerOrderWithObj{
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
    
    @ManyToOne
	@ToString.Exclude
    @JoinColumn(name="product_id")
    protected Product product;

    @ManyToOne
	@ToString.Exclude
    @JoinColumn(name="store_id")
    protected Store store;
}