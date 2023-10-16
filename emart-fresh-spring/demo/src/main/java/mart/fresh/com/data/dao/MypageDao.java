package mart.fresh.com.data.dao;

import java.time.LocalDateTime;
import mart.fresh.com.data.entity.Member;


public interface MypageDao {
	Member getMemberAndIsAppliedByMemberId(String memberId);
	int changePassword(String memberId, String memberPw, String newPw);
	int checkEmail(String memberEmail);
	int saveVerificationCode(String memberId, String verificationCode, LocalDateTime expiryTime);
	int changeEmail(String memberId, String newEmail, String verificationCode);
}