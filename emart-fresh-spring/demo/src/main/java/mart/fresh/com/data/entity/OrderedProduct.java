package mart.fresh.com.data.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

import org.apache.commons.lang3.builder.ToStringExclude;

@Data
@Entity
@Table(name = "ordered_product")
public class OrderedProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ordered_product_id")
    private int orderedProductId;

    @Column(name = "ordered_quantity")
    private int orderedQuantity;

    @Column(name = "is_pickup")
    private boolean isPickup;

    @Column(name = "ordered_del")
    private boolean orderedDel;

    @Column(name = "ordered_date")
    private Timestamp orderedDate;

    @Column(name = "total_amount")
    private int totalAmount;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "store_id")
    private Store store;
    
    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private Member member;
    
    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "coupon_id")
    private Coupon coupon;

	
}