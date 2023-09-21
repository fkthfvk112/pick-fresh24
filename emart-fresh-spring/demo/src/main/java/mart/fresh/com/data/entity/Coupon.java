package mart.fresh.com.data.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

import java.sql.Timestamp;

@Data
@Entity
@Table(name = "coupon")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id")
    private int couponId;

    @ManyToOne
    @ToString.Exclude
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(name = "coupon_expiration_date")
    private Timestamp couponExpirationDate;

    @Column(name = "coupon_type")
    private int couponType;
    
    @Column(name = "coupon_title")
    private String couponTitle;

    @Column(name = "coupon_del")
    private boolean couponDel;
}