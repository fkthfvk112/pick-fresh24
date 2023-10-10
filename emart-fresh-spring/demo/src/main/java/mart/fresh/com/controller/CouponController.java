package mart.fresh.com.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	@GetMapping("/coupon-all")
	public ResponseEntity<Page<CouponDto>> allCouponList(Authentication authentication, @RequestParam int page,
			@RequestParam int size) {

		String memberId = null;

		if (authentication != null) {
			memberId = authentication.getName();
		}
		Page<CouponDto> exceptCouponList = couponService.exceptCouponList(memberId, page - 1, size);
		return ResponseEntity.ok(exceptCouponList);
	}

	@GetMapping("/coupon-list")
	public ResponseEntity<Page<CouponDto>> myCouponList(Authentication authentication, @RequestParam int page,
			@RequestParam int size) {
		System.out.println("멤버 아이디" + authentication.getName());
		Page<CouponDto> couponList = couponService.myCouponList(authentication.getName(), page - 1, size);
			return ResponseEntity.ok(couponList);
		} 
	

	@PostMapping("/coupon-create")
	public ResponseEntity<String> createCoupon(Authentication authentication, @RequestBody CouponDto couponDto) {
		System.out.println("CouponController createCoupon");

		try {
			couponDto.setMemberId(authentication.getName());
			couponService.createCoupon(couponDto);

			System.out.println("쿠폰생성 완료");
			return ResponseEntity.ok("쿠폰생성 완료");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("쿠폰생성 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예외 에러 : " + e.getMessage());
		}
	}

	@PostMapping("/coupon-down")
	public ResponseEntity<String> couponDown(Authentication authentication, @RequestBody CouponDto couponDto) {
		System.out.println("CouponController couponDown");

		try {
			couponDto.setMemberId(authentication.getName());
			boolean isS = couponService.couponDown(couponDto);

			if (isS == false) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복쿠폰다운");
			}

			System.out.println("쿠폰다운 완료");
			return ResponseEntity.ok("쿠폰다운 완료");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("쿠폰다운 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예외 에러 : " + e.getMessage());
		}
	}

}