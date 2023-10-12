package mart.fresh.com.data.dao;

import java.time.LocalDateTime;
import java.util.List;
import mart.fresh.com.data.dto.StoreSalseDto;
import mart.fresh.com.data.entity.Member;


public interface MypageDao {
	Member getMemberAndIsAppliedByMemberId(String memberId);
	int changePassword(String memberId, String memberPw, String newPw);
	int checkEmail(String newEmail);
	int saveVerificationCode(String memberId, String verificationCode, LocalDateTime expiryTime);
	int changeEmail(String memberId, String newEmail, String verificationCode);
	List<StoreSalseDto> salesChart(String memberId);
}