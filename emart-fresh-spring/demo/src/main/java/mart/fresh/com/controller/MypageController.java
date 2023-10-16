package mart.fresh.com.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import mart.fresh.com.data.dto.StoreSalesAmountDto;
import mart.fresh.com.data.dto.StoreSalesProductTypeDto;
import mart.fresh.com.service.EmailService;
import mart.fresh.com.service.MemberService;
import mart.fresh.com.service.MypageService;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RequestMapping("/mypage")
@RestController
public class MypageController {
	private final MypageService mypageService;
	private final EmailService emailService;
	private final MemberService memberService;

	@Autowired
	public MypageController(MypageService mypageService, EmailService emailService, MemberService memberService) {
		this.mypageService = mypageService;
		this.emailService = emailService;
		this.memberService = memberService;
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

	@PostMapping("/mypage-checkemail")
	public ResponseEntity<String> checkEmail(Authentication authentication, @RequestBody String newEmail) {
		System.out.println("MypageController checkEmail : " + newEmail);

		if (StringUtils.isEmpty(newEmail)) {
			return ResponseEntity.badRequest().body("입력 값이 없습니다.");
		}

		JSONObject jsonobject = new JSONObject(newEmail);
		String memberEmail = jsonobject.getString("newEmail").trim();

		boolean isS = mypageService.checkEmail(memberEmail);

		System.out.println(" mypageService.checkEmail   mypageService.checkEmail : " + isS);

		if (isS) {
			return ResponseEntity.badRequest().body("사용중인 이메일");
		}

		MypageDto member = mypageService.getMemberAndIsAppliedByMemberId(authentication.getName());

		if (member != null) {
			String recipientEmail = memberEmail; // 수신자 이메일 주소 설정
			String subject = "[emart24 fresh] 이메일 변경 건 인증번호 메일입니다.";

			String verificationCode = generateVerificationCode();

			LocalDateTime currentTime = LocalDateTime.now();
			LocalDateTime expiryTime = currentTime.plusMinutes(5);

			emailService.sendEmailVerificationCode(member.getMemberName(), recipientEmail, subject, verificationCode);
			mypageService.saveVerificationCode(authentication.getName(), verificationCode, expiryTime);

			System.out.println("이메일변경 메일발송 성공 이메일 변경 인증번호 : " + verificationCode);
			return ResponseEntity.ok("이메일변경 메일발송 성공");

		}
		return ResponseEntity.badRequest().body("이메일변경 메일발송 실패");
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

	@PostMapping("/mypage-changeemail")
	public ResponseEntity<String> changeEmail(Authentication authentication, @RequestBody MemberDto memberDto) {

		System.out.println("MypageController changeEmail");

		String newEmail = memberDto.getMemberEmail();
		String verificationCode = memberDto.getVerifyCode();

		System.out.println("MypageController changeEmail verificationCode verificationCode: " + memberDto.toString());

		int count = mypageService.changeEmail(authentication.getName(), newEmail, verificationCode);

		if (count == 1) {
			return ResponseEntity.ok("이메일변경 성공");
		} else {
			return ResponseEntity.badRequest().body("이메일변경 실패");
		}
	}

	private Timestamp convertToTimestamp(LocalDateTime dateTime) {
		return Timestamp.valueOf(dateTime);
	}
	
	
	

//	@GetMapping("/saleschart")
//	public ResponseEntity<List<StoreSalesAmountDto>> salesChart(@RequestParam String memberId,
//			@RequestParam String startDate, @RequestParam String endDate) {
//
//		System.out.println("MypageController salesChart");
//
////		String memberId = authentication.getName();
//		int memberAuth = memberService.findMemberAuthByMemberId(memberId);
//
//		// 추후 ExceptionHandler로 점주권한이 필요하다는 메세지 추가 필요
//		if (memberAuth != 1) {
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//		}
//
//		
//		try {
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//			LocalDate startDateParsed = LocalDate.parse(startDate, formatter);
//			LocalDateTime startParsedDateTime = startDateParsed.atStartOfDay();
//			Timestamp startParsedTimestamp = convertToTimestamp(startParsedDateTime);
//
//			LocalDate endDateParsed = LocalDate.parse(endDate, formatter);
//			LocalDateTime endParsedDateTime = endDateParsed.atStartOfDay();
//			Timestamp endParsedTimestamp = convertToTimestamp(endParsedDateTime);
//
//			System.out.println(
//					"startParsedTimestamp : " + startParsedTimestamp + " endParsedTimestamp : " + endParsedTimestamp);
//
//			List<StoreSalesAmountDto> salesList = mypageService.salesChart(memberId, startParsedTimestamp,
//					endParsedTimestamp);
//
//			System.out.println("MypageController salesChart MypageController salesChart : " + salesList.toString());
//			return ResponseEntity.ok(salesList);
//		} catch (Exception e) {
//			System.out.println("에러났어 : " + e.getStackTrace());
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		}
//	}

	
	@GetMapping("/saleschart")
//	public ResponseEntity<List<StoreSalesAmountDto>> salesChart(@RequestParam String memberId, @RequestParam String searchDate, @RequestParam String period) {
	public ResponseEntity<List<StoreSalesAmountDto>> salesChart(Authentication authentication, @RequestParam String searchDate, @RequestParam String period) {

		System.out.println("MypageController salesChart");

		String memberId = authentication.getName();
		int memberAuth = memberService.findMemberAuthByMemberId(memberId);

		// 추후 ExceptionHandler로 점주권한이 필요하다는 메세지 추가 필요
		if (memberAuth != 1) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}

		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate searchDateParsed = LocalDate.parse(searchDate, formatter);
			LocalDateTime searchDateTime = searchDateParsed.atStartOfDay();
			Timestamp searchTimestamp = convertToTimestamp(searchDateTime);


			System.out.println("searchTimestamp : " + searchTimestamp);

			List<StoreSalesAmountDto> salesList = mypageService.salesChart(memberId, searchTimestamp, period);

			System.out.println("MypageController salesChart MypageController salesChart : " + salesList.toString());
			return ResponseEntity.ok(salesList);
		} catch (Exception e) {
			System.out.println("에러났어 : " + e.getStackTrace());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
	@GetMapping("/typechart")
	public ResponseEntity<List<StoreSalesProductTypeDto>> typeChart(@RequestParam String memberId, @RequestParam String searchDate, @RequestParam String period) {

		System.out.println("MypageController typechart");

//		String memberId = authentication.getName();
		int memberAuth = memberService.findMemberAuthByMemberId(memberId);

		// 추후 ExceptionHandler로 점주권한이 필요하다는 메세지 추가 필요
		if (memberAuth != 1) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
		}

		
		try {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			LocalDate searchDateParsed = LocalDate.parse(searchDate, formatter);
			LocalDateTime searchDateTime = searchDateParsed.atStartOfDay();
			Timestamp searchTimestamp = convertToTimestamp(searchDateTime);


			System.out.println("searchTimestamp : " + searchTimestamp);

			List<StoreSalesProductTypeDto> typeChartList = mypageService.productTypeChart(memberId, searchTimestamp, period);

			System.out.println("MypageController typechart MypageController typechart : " + typeChartList.toString());
			return ResponseEntity.ok(typeChartList);
		} catch (Exception e) {
			System.out.println("에러났어 : " + e.getStackTrace());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}
	
//	@GetMapping("/typechart")
//	public ResponseEntity<List<StoreSalesProductTypeDto>> typeChart(@RequestParam String memberId,
//			@RequestParam String startDate, @RequestParam String endDate) {
//
//		System.out.println("MypageController typeChart");
//
////		String memberId = authentication.getName();
//		int memberAuth = memberService.findMemberAuthByMemberId(memberId);
//
//		// 추후 ExceptionHandler로 점주권한이 필요하다는 메세지 추가 필요
//		if (memberAuth != 1) {
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//		}
//		
//		
//		try {
//			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
//
//			LocalDate startDateParsed = LocalDate.parse(startDate, formatter);
//			LocalDateTime startParsedDateTime = startDateParsed.atStartOfDay();
//			Timestamp startParsedTimestamp = convertToTimestamp(startParsedDateTime);
//
//			LocalDate endDateParsed = LocalDate.parse(endDate, formatter);
//			LocalDateTime endParsedDateTime = endDateParsed.atStartOfDay();
//			Timestamp endParsedTimestamp = convertToTimestamp(endParsedDateTime);
//
//			System.out.println(
//					"startParsedTimestamp : " + startParsedTimestamp + " endParsedTimestamp : " + endParsedTimestamp);
//
//			List<StoreSalesProductTypeDto> salesProductTypeList = mypageService.salesProductTypeList(memberId, startParsedTimestamp,
//					endParsedTimestamp);
//
//			System.out.println("MypageController salesChart MypageController salesChart : " + salesProductTypeList.toString());
//			return ResponseEntity.ok(salesProductTypeList);
//		} catch (Exception e) {
//			System.out.println("StoreSalesProductTypeDto 에러났어 : " + e.getStackTrace());
//			e.printStackTrace();
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//		}
//	}

//	@GetMapping("/titlechart")
//	public ResponseEntity<String> titleChart(Authentication authentication, Timestamp date) {
//
//		System.out.println("MypageController titleChart");
//
//		int memberAuth = memberService.findMemberAuthByMemberId(authentication.getName());
//		int memberAuth = memberService.findMemberAuthByMemberId(memberId);
//
////	 추후 ExceptionHandler로 점주권한이 필요하다는 메세지 추가 필요
//		if (memberAuth != 1)
//
//		{
//			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
//		}
//
//		if (date == null) {
//			date = new Timestamp(System.currentTimeMillis());
//		}
//
//		List<StoreSalseDto> count = mypageService.salesChart(authentication.getName());
//
//		return ResponseEntity.ok("인기물품 순위 표시");
//	}

}