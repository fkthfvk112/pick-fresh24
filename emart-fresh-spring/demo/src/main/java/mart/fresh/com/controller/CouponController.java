package mart.fresh.com.controller;

import java.util.Date;
import java.util.Map;
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
import mart.fresh.com.service.MemberService;

@RequestMapping("/coupon")
@RestController
public class CouponController {

	private final CouponService couponService;
	private final MemberService memberService;

	@Autowired
	public CouponController(CouponService couponService, MemberService memberService) {
		this.couponService = couponService;
		this.memberService = memberService;
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
		
		int memberAuth = memberService.findMemberAuthByMemberId(authentication.getName());
		
		if(memberAuth != 2) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 필요합니다.");
		}
		
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
		
		
		int memberAuth = memberService.findMemberAuthByMemberId(authentication.getName());
		
		if(memberAuth != 0) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("일반회원만 쿠폰을 받을 수 있습니다.");
		}

		try {
			couponDto.setMemberId(authentication.getName());
			boolean isS = couponService.couponDown(couponDto);

			if (isS == false) {
				return ResponseEntity.status(HttpStatus.CONFLICT).body("중복쿠폰다운");
			}

			System.out.println("쿠폰다운 완료");
			return ResponseEntity.ok("쿠폰다운 완료");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("쿠폰다운 실패");
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예외 에러 : " + e.getMessage());
		}
	}
	
	@PostMapping("/coupon-delete")
	public ResponseEntity<String> couponDelete(Authentication authentication, @RequestBody Map<String, Integer> request) {
	    try {
	        String memberId = authentication.getName();
	        System.out.println("CartController " + memberId + "님의 쿠폰 삭제 " + new Date());

	        if (!request.containsKey("couponId")) {
	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
	                .body("couponId를 보내주세요.");
	        }

	        int couponId = request.get("couponId");
	        couponService.deleteByMemberMemberIdAndCouponId(memberId, couponId);

	        return ResponseEntity.status(HttpStatus.OK).build();
	    } catch (Exception e) {
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body("coupon-delete error : " + e.getMessage());
	    }
	}


}