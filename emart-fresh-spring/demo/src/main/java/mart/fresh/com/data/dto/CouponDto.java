package mart.fresh.com.data.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class CouponDto {
    private int couponId;
    private String memberId;
    private Timestamp couponExpirationDate;
    private int couponType;
    private String couponTitle;
}