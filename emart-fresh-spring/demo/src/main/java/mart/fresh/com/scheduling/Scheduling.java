//package mart.fresh.com.scheduling;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.EnableScheduling;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import mart.fresh.com.data.entity.Coupon;
//import mart.fresh.com.data.entity.Member;
//import mart.fresh.com.data.entity.Product;
//import mart.fresh.com.data.entity.RefreshToken;
//import mart.fresh.com.data.repository.CouponRepository;
//import mart.fresh.com.data.repository.MemberRepository;
//import mart.fresh.com.data.repository.ProductRepository;
//import mart.fresh.com.data.repository.RefreshTokenRepository;
//
//@EnableScheduling
//@Component
//public class Scheduling {
//	
//	private MemberRepository memberRepository;
//	private RefreshTokenRepository refreshTokenRepository;
//	private CouponRepository couponRepository;
//	private ProductRepository productRepository;
//
//	@Autowired
//    public Scheduling(MemberRepository memberRepository, RefreshTokenRepository refreshTokenRepository,
//    					CouponRepository couponRepository, ProductRepository productRepository) {
//        this.memberRepository = memberRepository;
//        this.refreshTokenRepository = refreshTokenRepository;
//        this.couponRepository = couponRepository;
//        this.productRepository = productRepository;
//        
//    }
//	
//	@Scheduled(fixedDelay = 60001) // 매 60초마다 실행
//    public void processExpiredVerificationCode() {
//		
//		LocalDateTime currentTime = LocalDateTime.now();
//        List<Member> expiredMembers = memberRepository.findExpiredVerificationCodes(currentTime);
//        
//        for (Member member : expiredMembers) {
//            // 만료된 인증 코드 처리 로직
//            member.setVerifyCode(null);
//            member.setVerifyCodeExpiry(null);
//            memberRepository.save(member);
//        }
//    }
//	
//	@Scheduled(fixedDelay = 60*60*1000) // 매 60분마다 실행
//	public void processExpiredRefreshToken() {
//		LocalDateTime currentTime = LocalDateTime.now();
//	    List<RefreshToken> expiredRefreshTokens = refreshTokenRepository.findExpiredRefreshTokens(currentTime);
//	    
//	    for (RefreshToken refreshToken : expiredRefreshTokens) {
//	        // 만료된 Refresh Token 처리 로직
//	        refreshToken.setRefreshToken(null);
//	        refreshToken.setRefreshTokenExpiry(null);
//	        refreshTokenRepository.save(refreshToken);
//	    }
//	}
//
//
//	@Scheduled(fixedDelay = 1800001) // 매 30분마다 실행
//	public void processChangeProductTimeSale() {
//		
//		
//		LocalDateTime currentTime = LocalDateTime.now();
//		LocalDateTime sixHoursFromNow = currentTime.plusHours(6);
//
//		
//      List<Product> changeProductTimeSale = productRepository.findProductTimeSale(sixHoursFromNow);
//      
//      for (Product product : changeProductTimeSale) {
//
//      	product.setProductTimeSale(30);
//      	productRepository.save(product);
//      }
//  }
//	
//	
//	@Scheduled(fixedDelay = 60*1000*10) // 매 10분마다 실행
//	public void processExpiredCoupon() {
//		  LocalDateTime currentTime = LocalDateTime.now();
//		    
//		    List<Coupon> expiredCoupons = couponRepository.findByCouponExpirationDateBefore(currentTime);
//		    
//		    for (Coupon coupon : expiredCoupons) {
//		        coupon.setCouponDel(true); 
//		        couponRepository.save(coupon); 
//		    }
//    }
//}
