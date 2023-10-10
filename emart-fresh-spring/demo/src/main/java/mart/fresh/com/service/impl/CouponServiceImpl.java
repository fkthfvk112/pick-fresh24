package mart.fresh.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import mart.fresh.com.data.dao.CouponDao;
import mart.fresh.com.data.dto.CouponDto;
import mart.fresh.com.data.entity.Coupon;
import mart.fresh.com.service.CouponService;

@Service
public class CouponServiceImpl implements CouponService {
	private final CouponDao couponDao;

	@Autowired
	public CouponServiceImpl(CouponDao couponDao) {
		this.couponDao = couponDao;
	}

	private CouponDto convertEntityToDto(Coupon entity) {
		CouponDto dto = new CouponDto();
		dto.setCouponId(entity.getCouponId());
		dto.setMemberId(entity.getMember().getMemberId());
		dto.setCouponExpirationDate(entity.getCouponExpirationDate());
		dto.setCouponType(entity.getCouponType());
		dto.setCouponTitle(entity.getCouponTitle());

		return dto;
	}

	public Page<CouponDto> myCouponList(String memberId, int page, int size) {
		System.out.println("CouponServiceImpl myCouponList");
		Page<Coupon> couponList = couponDao.myCouponList(memberId, page, size);
		Page<CouponDto> couponDtoPage = couponList.map(this::convertEntityToDto);
		return couponDtoPage;
	}

	public boolean createCoupon(CouponDto couponDto) {
		System.out.println("CouponServiceImpl createCoupon");
		return couponDao.createCoupon(couponDto) > 0 ? true : false;

	}

	public boolean couponDown(CouponDto couponDto) {
		System.out.println("CouponServiceImpl couponDown");
		return couponDao.couponDown(couponDto) > 0 ? true : false;
	}

	@Override
	public Coupon findByCouponId(int couponId) {
		return couponDao.findByCouponId(couponId);
	}

	public Page<CouponDto> exceptCouponList(String memberId, int page, int size) {
		Page<CouponDto> couponList = couponDao.exceptCouponList(memberId, page, size);
		return couponList;
	}

	@Override
	public void deleteByMemberMemberIdAndCouponId(String memberId, int couponId) {
		couponDao.deleteByMemberMemberIdAndCouponId(memberId, couponId);
		
	}

}