package mart.fresh.com.data.dao.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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

		if (couponExpirationDate != null) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(couponExpirationDate.getTime());
			calendar.set(Calendar.HOUR_OF_DAY, 23);
			calendar.set(Calendar.MINUTE, 59);
			calendar.set(Calendar.SECOND, 59);
			couponExpirationDate.setTime(calendar.getTimeInMillis());
		}

		int couponType = couponDto.getCouponType();
		String couponTitle = couponDto.getCouponTitle();

		Member member = mypageRepository.findMemberByMemberId(couponDto.getMemberId());

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

		int count = couponRepository.countByCouponTypeAndCouponTitleAndCouponExpirationDateAndMemberMemberId (
				couponType, couponTitle, couponExpirationDate, couponDto.getMemberId());

		System.out.println("CouponDaoImpl countByCouponTypeAndCouponTitleAndCouponExpirationDateAndMemberMemberId : " + count);

		if (count > 0) {
			return 0;
		} else {

			// Coupon 엔티티 생성
			Coupon coupon = new Coupon();
			coupon.setCouponExpirationDate(couponExpirationDate);
			coupon.setCouponType(couponType);
			coupon.setCouponTitle(couponTitle);
			coupon.setMember(member);

			couponRepository.save(coupon);

			return 1;
		}
	}

	@Override
	public Coupon findByCouponId(int couponId) {
		return couponRepository.findByCouponId(couponId);
	}

	public Page<CouponDto> exceptCouponList(String memberId, int page, int size) {
		System.out.println("CouponDaoImpl exceptCouponList : " + memberId);
		Pageable pageable = PageRequest.of(page, size);
		int memberAuth = 2;

		Page<Coupon> couponList = new PageImpl<Coupon>(Collections.emptyList());
		List<CouponDto> responseList = new ArrayList<>();

		couponList = couponRepository.exceptCouponList(pageable, memberAuth);
		long totalCount = couponList.getTotalElements();
		System.out.println("Total count of coupons: " + totalCount);

		if (memberId != null) {
			for (Coupon coupon : couponList.getContent()) {
				int count = couponRepository.countByCouponTypeAndCouponTitleAndCouponExpirationDateAndMemberMemberId(
						coupon.getCouponType(), coupon.getCouponTitle(), coupon.getCouponExpirationDate(), memberId);
				boolean isExisting = count > 0;

				CouponDto couponDto = new CouponDto();
				couponDto.setCouponId(coupon.getCouponId());
				couponDto.setMemberId(coupon.getMember().getMemberId());
				couponDto.setCouponExpirationDate(coupon.getCouponExpirationDate());
				couponDto.setCouponType(coupon.getCouponType());
				couponDto.setCouponTitle(coupon.getCouponTitle());
				couponDto.setExisting(isExisting);

				responseList.add(couponDto);
			}
		} else {
			for (Coupon coupon : couponList.getContent()) {
				CouponDto couponDto = new CouponDto();
				couponDto.setCouponId(coupon.getCouponId());
				couponDto.setMemberId(coupon.getMember().getMemberId());
				couponDto.setCouponExpirationDate(coupon.getCouponExpirationDate());
				couponDto.setCouponType(coupon.getCouponType());
				couponDto.setCouponTitle(coupon.getCouponTitle());
				couponDto.setExisting(false);

				responseList.add(couponDto);
			}
		}

		Page<CouponDto> responses = new PageImpl<>(responseList, pageable, totalCount);

		return responses;
	}

	@Override
	@Transactional
	public void deleteByMemberMemberIdAndCouponId(String memberId, int couponId) {
		couponRepository.deleteByMemberMemberIdAndCouponId(memberId, couponId);

	}

	@Override
	public void updateCouponDel(String memberId, int couponId) {
		Coupon coupon = couponRepository.findByCouponId(couponId);
		coupon.setCouponDel(true);
		couponRepository.save(coupon);
	}

}