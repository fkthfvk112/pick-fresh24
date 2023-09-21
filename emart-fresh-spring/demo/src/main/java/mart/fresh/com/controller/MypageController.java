package mart.fresh.com.controller;


import java.time.LocalDateTime;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.MypageDto;
import mart.fresh.com.service.EmailService;
import mart.fresh.com.service.MypageService;


@RequestMapping("/mypage")
@RestController
public class MypageController {
	private final MypageService mypageService;
	private final EmailService emailService;
	
	@Autowired
	public MypageController(MypageService mypageService, EmailService emailService) {
		this.mypageService = mypageService;
		this.emailService = emailService;
	}
	
    // 인증번호 생성 (임의로 6자리 숫자로 생성하도록 설정)
 	private String generateVerificationCode() {
 		Random random = new Random();
 		int code = 100000 + random.nextInt(900000);
 		return String.valueOf(code);
 	}
	
	
	@PostMapping("/mypage-info")
	public MypageDto getMemberByMemberId(@RequestParam String memberId) {
		System.out.println("MypageController getMemberByMemberId");
		
		MypageDto mypageInfo = mypageService.getMemberAndIsAppliedByMemberId(memberId);
		
		return mypageInfo;
	}
	
	@PostMapping("/mypage-changepassword")
	public String changePassword(@RequestParam String memberId,
									@RequestParam String memberPw,
									@RequestParam String newPw) {
		System.out.println("MypageController changepassword");
		
		boolean isS = mypageService.changePassword(memberId, memberPw, newPw);
		
		if(isS) { return "Success";	}
			else { return "fail"; }
	}
	
	
	@PostMapping("/mypage-checkemail")
	public String checkEmail(@RequestParam String memberId,
								@RequestParam String newEmail) {
		System.out.println("MypageController checkEmail");
		
		boolean isS = mypageService.checkEmail(newEmail);
		
		if(isS) { return "사용 중인 이메일입니다."; }

		MypageDto member = mypageService.getMemberAndIsAppliedByMemberId(memberId);
		
			if(member != null) {
			  String recipientEmail = newEmail; // 수신자 이메일 주소 설정
	          String subject = "[emart24 fresh] 이메일 변경 건 인증번호 메일입니다.";
	 		 
	          String verificationCode = generateVerificationCode();
	          
	 		 LocalDateTime currentTime = LocalDateTime.now();
	         LocalDateTime expiryTime = currentTime.plusMinutes(5);
	        
	  		emailService.sendEmailVerificationCode(member.getMemberName(), recipientEmail, subject, verificationCode);
	  		mypageService.saveVerificationCode(memberId, verificationCode, expiryTime);
	  		
			return "Success"; 
			}
			
			return "fail";			
		
	}

	
	
	@PostMapping("/mypage-changeemail")
	public String changeEmail(@RequestParam String memberId,
								@RequestParam String newEmail,
								@RequestParam String verificationCode) {
		
		System.out.println("MypageController changeEmail");
		

         
		int count = mypageService.changeEmail(memberId, newEmail, verificationCode);

		
		
		if( count == 0 ) { return "인증번호를 확인해주세요"; }
		else { return "메일 변경이 완료되었습니다"; }	
	}		
		

}