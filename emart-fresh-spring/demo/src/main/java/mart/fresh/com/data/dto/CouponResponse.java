package mart.fresh.com.data.dto;


import lombok.Data;
import mart.fresh.com.data.entity.Coupon;

@Data
public class CouponResponse {
    private Coupon coupon;
    private boolean isExisting;

    public CouponResponse(Coupon coupon, boolean isExisting) {
        this.coupon = coupon;
        this.isExisting = isExisting;
    }
}
