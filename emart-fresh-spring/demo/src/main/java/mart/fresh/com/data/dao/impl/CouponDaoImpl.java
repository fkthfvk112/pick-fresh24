package mart.fresh.com.data.dao.impl;

import java.sql.Timestamp;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import mart.fresh.com.data.dao.CouponDao;
import mart.fresh.com.data.dto.CouponDto;
import mart.fresh.com.data.entity.Coupon;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.repository.CouponRepository;
import mart.fresh.com.data.repository.MypageRepository;

@Component
public class CouponDaoImpl implements CouponDao {
	private final CouponRepository couponRepository;
	private final MypageRepository mypageRepository;

	
	@Autowired
	public CouponDaoImpl(CouponRepository couponRepository, MypageRepository mypageRepository) {
		this.couponRepository = couponRepository;
		this.mypageRepository = mypageRepository;
	}
	
	public Page<Coupon> myCouponList(String memberId, int page, int size) {
		System.out.println("CouponDaoImpl myCouponList");
	    Pageable pageable = PageRequest.of(page, size);  
		Page<Coupon> couponList = couponRepository.myCouponList(memberId, pageable);
		return couponList;
	}
	

	public int createCoupon(CouponDto couponDto) {
		
	    
	    // CouponDto에서 필요한 데이터 추출
	    Timestamp couponExpirationDate = couponDto.getCouponExpirationDate();
	    int couponType = couponDto.getCouponType();
	    String couponTitle = couponDto.getCouponTitle();

		Member member = mypageRepository.findMemberByMemberId("admin");
	    
	    // Coupon 엔티티 생성
	    Coupon coupon = new Coupon();
	    coupon.setCouponExpirationDate(couponExpirationDate);
	    coupon.setCouponType(couponType);
	    coupon.setCouponTitle(couponTitle);
	    coupon.setMember(member);
	    coupon.setCouponDel(false);
	    
	    System.out.println("CouponDaoImpl createCoupon" + coupon.toString());
	    
	    // Coupon 엔티티를 저장하고 반환된 ID를 받음
	    Coupon savedCoupon = couponRepository.save(coupon);
	    


	    // 저장된 Coupon 엔티티의 ID 반환
	    return savedCoupon.getCouponId();
	}

	
	public int couponDown(CouponDto couponDto) {
		System.out.println("CouponDaoImpl couponDown");
		
		// CouponDto에서 필요한 데이터 추출
		
	    Timestamp couponExpirationDate = couponDto.getCouponExpirationDate();
	    int couponType = couponDto.getCouponType();
	    String couponTitle = couponDto.getCouponTitle();

		Member member = mypageRepository.findMemberByMemberId(couponDto.getMemberId());
	    
	    // Coupon 엔티티 생성
	    Coupon coupon = new Coupon();
	    coupon.setCouponExpirationDate(couponExpirationDate);
	    coupon.setCouponType(couponType);
	    coupon.setCouponTitle(couponTitle);
	    coupon.setMember(member);

	    System.out.println("CouponDaoImpl couponDown22" + coupon.toString());
	    
	    // Coupon 엔티티를 저장하고 반환된 ID를 받음
	    Coupon savedCoupon = couponRepository.save(coupon);
	    
	    return savedCoupon.getCouponId();
	}
	
	@Override
	public Coupon findByCouponId(int couponId) {
		return couponRepository.findByCouponId(couponId);
	}
	
	public Page<Coupon> AllCouponList(int page, int size) {
		System.out.println("CouponDaoImpl myCouponList");
	    Pageable pageable = PageRequest.of(page, size);
		Page<Coupon> couponList = couponRepository.AllCouponList(pageable);
		return couponList;
	}
}