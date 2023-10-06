package mart.fresh.com.data.dao;

import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.CouponDto;
import mart.fresh.com.data.entity.Coupon;

public interface CouponDao {

	Page<Coupon> myCouponList(String memberId, int page, int size);
	int createCoupon(CouponDto couponDto);
	int couponDown(CouponDto couponDto);
	Coupon findByCouponId(int couponId);
	Page<CouponDto> exceptCouponList(String memberId, int page, int size);
}