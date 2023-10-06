package mart.fresh.com.service;

import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.CouponDto;
import mart.fresh.com.data.dto.CouponResponse;
import mart.fresh.com.data.entity.Coupon;

public interface CouponService {

	Page<CouponDto> myCouponList(String memberId, int page, int size);

	boolean createCoupon(CouponDto couponDto);

	boolean couponDown(CouponDto couponDto);

	Coupon findByCouponId(int couponId);

	Page<CouponResponse> exceptCouponList(String memberId, int page, int size);
	
}