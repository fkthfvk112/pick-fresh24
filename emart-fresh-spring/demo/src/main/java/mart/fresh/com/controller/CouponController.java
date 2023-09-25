package mart.fresh.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.CouponDto;
import mart.fresh.com.service.CouponService;


@RequestMapping("/coupon")
@RestController
public class CouponController {

	private final CouponService couponService;
	
	@Autowired
	public CouponController(CouponService couponService) {
		this.couponService = couponService;
	}
	
	@GetMapping("/coupon-list")
	public Page<CouponDto> myCouponList(Authentication authentication,
										@RequestParam int page, @RequestParam int size) {
		
		Page<CouponDto> couponList = couponService.myCouponList(authentication.getName(), page-1, size);
		return couponList;
	}

	
	@PostMapping("/coupon-create")
	public String createCoupon(@RequestBody CouponDto couponDto) {
	    System.out.println("CouponController createCoupon");

		boolean isS = couponService.createCoupon(couponDto);
		
		if(isS) { return "쿠폰이 생성되었습니다.";	}
		else { return "쿠폰생성이 실패했습니다."; }
	}
	
	@PostMapping("/coupon-down")
	public String couponDown(@RequestBody CouponDto couponDto) {
		System.out.println("CouponController couponDown");
		
		boolean isS = couponService.couponDown(couponDto);
		
		if(isS) { return "쿠폰을 받았습니다.";	}
		else { return "쿠폰받기에 실패했습니다."; }
	}
	
	

}