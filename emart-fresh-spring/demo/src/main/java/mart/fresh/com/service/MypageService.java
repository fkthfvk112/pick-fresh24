package mart.fresh.com.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import mart.fresh.com.data.dto.MypageDto;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.StoreSalesAmountDto;
import mart.fresh.com.data.dto.StoreSalesProductTitleDto;
import mart.fresh.com.data.dto.StoreSalesProductTypeDto;


public interface MypageService {
	MypageDto getMemberAndIsAppliedByMemberId(String memberId);
	boolean changePassword(String memberId, String memberPw, String newPw);
	boolean checkEmail(String memberEmail);
	boolean saveVerificationCode(String memberId, String verificationCode, LocalDateTime expiryTime);
	int changeEmail(String memberId, String newEmail, String verificationCode);
	List<StoreSalesAmountDto> salesChart(String memberId, Timestamp searchDate, String period);
	List<StoreSalesProductTypeDto> productTypeChart(String memberId, Timestamp searchDate, String period);
	List<StoreSalesProductTitleDto> productTitleChart(String memberId, Timestamp searchDate, String period);
	boolean productRegistration(ProductDto dto);
}