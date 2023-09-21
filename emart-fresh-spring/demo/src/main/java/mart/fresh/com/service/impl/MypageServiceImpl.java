package mart.fresh.com.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import mart.fresh.com.data.dao.MypageDao;
import mart.fresh.com.data.dto.MypageDto;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.repository.MypageRepository;
import mart.fresh.com.service.MypageService;

@Service
public class MypageServiceImpl implements MypageService {

	private final MypageDao mypageDao;
	private final MypageRepository mypageRepository;
	
	@Autowired
	public MypageServiceImpl(MypageDao mypageDao, MypageRepository mypageRepository) {
		this.mypageDao = mypageDao;
		this.mypageRepository = mypageRepository;
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
		 if(applyManagerEntity != null) {
		 resultMemberInfo.setApplied(applyManagerEntity.isApplied());
		 } else { resultMemberInfo.setApplied(true); }
		 
		return resultMemberInfo;
	}

	@Override
	public boolean changePassword(String memberId, String memberPw, String newPw) {
		System.out.println("MypageServiceImpl changePassword");
		
		return mypageDao.changePassword(memberId, memberPw, newPw)>0?true:false;
	}

	
	@Override
	public boolean checkEmail(String newEmail) {
		return mypageDao.checkEmail(newEmail)>0?true:false;
	}
	
	
	@Override
	public boolean saveVerificationCode(String memberId, String verificationCode, LocalDateTime expiryTime) {
		return mypageDao.saveVerificationCode(memberId, verificationCode, expiryTime)>0?true:false;
		
	}

	
	@Override
	public int changeEmail(String memberId, String newEmail, String verificationCode) {
		return mypageDao.changeEmail(memberId, newEmail, verificationCode);
	}



	
}