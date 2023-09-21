package mart.fresh.com.service;


import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.CouponDto;

public interface CouponService {

	Page<CouponDto> myCouponList(String memberId, int page, int size);
	boolean createCoupon(CouponDto couponDto);
	boolean couponDown(CouponDto couponDto);
}