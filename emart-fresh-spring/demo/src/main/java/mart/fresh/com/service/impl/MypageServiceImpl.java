package mart.fresh.com.service.impl;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.MypageDao;
import mart.fresh.com.data.dao.OrderedProductDao;
import mart.fresh.com.data.dao.StoreDao;
import mart.fresh.com.data.dto.MypageDto;
import mart.fresh.com.data.dto.StoreSalesAmountDto;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.OrderedProduct;
import mart.fresh.com.data.entity.Store;
import mart.fresh.com.data.repository.MypageRepository;
import mart.fresh.com.service.MypageService;

@Service
public class MypageServiceImpl implements MypageService {

	private final MypageDao mypageDao;
	private final MypageRepository mypageRepository;
	private final OrderedProductDao orderedProductDao;
	private final StoreDao storeDao;
	
	@Autowired
	public MypageServiceImpl(MypageDao mypageDao,
							MypageRepository mypageRepository,
							OrderedProductDao orderedProductDao,
							StoreDao storeDao) {
		this.mypageDao = mypageDao;
		this.mypageRepository = mypageRepository;
		this.orderedProductDao = orderedProductDao;
		this.storeDao = storeDao;
	}

	@Override
	public MypageDto getMemberAndIsAppliedByMemberId(String memberId) {
		System.out.println("MypageServiceImpl getMemberAndIsAppliedByMemberId");

		Member entity = mypageDao.getMemberAndIsAppliedByMemberId(memberId);

		ApplyManager applyManagerEntity = mypageRepository.getMemberAndIsAppliedByMemberId(memberId);

		MypageDto resultMemberInfo = new MypageDto();
		resultMemberInfo.setMemberId(entity.getMemberId());
		resultMemberInfo.setMemberName(entity.getMemberName());
		resultMemberInfo.setMemberEmail(entity.getMemberEmail());
		resultMemberInfo.setMemberAuth(entity.getMemberAuth());
		resultMemberInfo.setMemberDel(entity.isMemberDel());
		if (applyManagerEntity != null) {
			resultMemberInfo.setApplied(applyManagerEntity.isApplied());
		} else {
			resultMemberInfo.setApplied(true);
		}

		return resultMemberInfo;
	}

	@Override
	public boolean changePassword(String memberId, String memberPw, String newPw) {
		System.out.println("MypageServiceImpl changePassword");

		return mypageDao.changePassword(memberId, memberPw, newPw) > 0 ? true : false;
	}

	@Override
	public boolean checkEmail(String memberEmail) {
		System.out.println("MypageServiceImpl MypageServiceImpl checkEmail : " + mypageDao.checkEmail(memberEmail));
		return mypageDao.checkEmail(memberEmail) > 0 ? true : false;
	}

	@Override
	public boolean saveVerificationCode(String memberId, String verificationCode, LocalDateTime expiryTime) {
		return mypageDao.saveVerificationCode(memberId, verificationCode, expiryTime) > 0 ? true : false;

	}

	@Override
	public int changeEmail(String memberId, String newEmail, String verificationCode) {
		return mypageDao.changeEmail(memberId, newEmail, verificationCode);
	}

//	@Override
//	public List<StoreSalesAmountDto> salesChart(String memberId, Timestamp startDate, Timestamp endDate) {
//
//		Store store = storeDao.findByMemberMemberId(memberId);
//		
//		List<OrderedProduct> salesEntityList = orderedProductDao.findByStoreStoreIdAndOrderedDateBetween(store.getStoreId(), startDate, endDate);
//		
//		List<StoreSalesAmountDto> salesList = new ArrayList<>();
//		
//	    for(OrderedProduct salesData : salesEntityList) {
//	        StoreSalesAmountDto dto = new StoreSalesAmountDto();
//	        dto.setOrderedDate(salesData.getOrderedDate());
//	        dto.setTotalAmount(salesData.getTotalAmount());
//	        
//	        salesList.add(dto);
//	    }
//	    
//	    return salesList;
//	}

}