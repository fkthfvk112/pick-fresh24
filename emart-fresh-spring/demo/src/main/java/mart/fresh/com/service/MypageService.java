package mart.fresh.com.service;

import java.time.LocalDateTime;
import java.util.List;
import mart.fresh.com.data.dto.MypageDto;
import mart.fresh.com.data.dto.StoreSalseDto;


public interface MypageService {
	MypageDto getMemberAndIsAppliedByMemberId(String memberId);
	boolean changePassword(String memberId, String memberPw, String newPw);
	boolean checkEmail(String newEmail);
	boolean saveVerificationCode(String memberId, String verificationCode, LocalDateTime expiryTime);
	int changeEmail(String memberId, String newEmail, String verificationCode);
	List<StoreSalseDto> salesChart(String memberId);
}