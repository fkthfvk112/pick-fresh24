package mart.fresh.com.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import mart.fresh.com.data.dto.MemberDto;
import mart.fresh.com.data.dto.MypageDto;
import mart.fresh.com.data.dto.StoreSalseDto;
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

	@GetMapping("/mypage-info")
	public ResponseEntity<MypageDto> getMemberByMemberId(Authentication authentication) {
		System.out.println("MypageController getMemberByMemberId");

		MypageDto mypageInfo = mypageService.getMemberAndIsAppliedByMemberId(authentication.getName());

		return ResponseEntity.ok(mypageInfo);
	}

	@PostMapping("/mypage-changepassword")
	public ResponseEntity<String> changePassword(Authentication authentication, @RequestBody MemberDto memberDto) {
		System.out.println("MypageController changepassword");

		boolean isS = mypageService.changePassword(authentication.getName(), memberDto.getMemberPw(),
				memberDto.getNewPw());

		if (isS) {
			return ResponseEntity.ok("비밀번호변경 성공");
		} else {
			return ResponseEntity.badRequest().body("비밀번호변경 실패");
		}
	}

	@PostMapping("/mypage-checkemail")
	public ResponseEntity<String> checkEmail(Authentication authentication, @RequestParam String newEmail) {
		System.out.println("MypageController checkEmail");

		boolean isS = mypageService.checkEmail(newEmail);

		if (isS) {
			return ResponseEntity.badRequest().body("사용중인 이메일");
		}

		MypageDto member = mypageService.getMemberAndIsAppliedByMemberId(authentication.getName());

		if (member != null) {
			String recipientEmail = newEmail; // 수신자 이메일 주소 설정
			String subject = "[emart24 fresh] 이메일 변경 건 인증번호 메일입니다.";

			String verificationCode = generateVerificationCode();

			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime expiryTime = currentTime.plusMinutes(5);

			emailService.sendEmailVerificationCode(member.getMemberName(), recipientEmail, subject, verificationCode);
			mypageService.saveVerificationCode(authentication.getName(), verificationCode, expiryTime);

			return ResponseEntity.ok("이메일변경 메일발송 성공");
		}
		return ResponseEntity.badRequest().body("이메일변경 메일발송 실패");
	}

	@PostMapping("/mypage-changeemail")
	public ResponseEntity<String> changeEmail(Authentication authentication, MemberDto memberDto) {

		System.out.println("MypageController changeEmail");

		String newEmail = memberDto.getMemberEmail();
		String verificationCode = memberDto.getVerifyCode();

		int count = mypageService.changeEmail(authentication.getName(), newEmail, verificationCode);

		if (count == 0) {
			return ResponseEntity.ok("이메일변경 성공");
		} else {
			return ResponseEntity.badRequest().body("이메일변경 실패");
		}
	}

	@GetMapping("/saleschart")
	public ResponseEntity<String> salesChart(Authentication authentication) {

		System.out.println("MypageController salesChart");

		List<StoreSalseDto> count = mypageService.salesChart(authentication.getName());

		return ResponseEntity.ok("매출현황 표시");
	}

}