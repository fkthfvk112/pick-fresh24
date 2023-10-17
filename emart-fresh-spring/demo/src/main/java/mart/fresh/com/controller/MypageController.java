package mart.fresh.com.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.json.JSONObject;
import org.springframework.util.StringUtils;
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
import org.springframework.web.multipart.MultipartFile;
import mart.fresh.com.data.dto.MemberDto;
import mart.fresh.com.data.dto.MypageDto;
import mart.fresh.com.data.dto.ProductDto;
import mart.fresh.com.data.dto.ProductRegistrationDto;
import mart.fresh.com.data.dto.StoreSalesAmountDto;
import mart.fresh.com.data.dto.StoreSalesProductTitleDto;
import mart.fresh.com.data.dto.StoreSalesProductTypeDto;
import mart.fresh.com.service.EmailService;
import mart.fresh.com.service.EventService;
import mart.fresh.com.service.MemberService;
import mart.fresh.com.service.MypageService;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

@RequestMapping("/mypage")
@RestController
public class MypageController {
	private final MypageService mypageService;
	private final EmailService emailService;
	private final MemberService memberService;
	private final EventService eventService;

	@Autowired
	public MypageController(MypageService mypageService, EmailService emailService, MemberService memberService,
			EventService eventService) {
		this.mypageService = mypageService;
		this.emailService = emailService;
		this.memberService = memberService;
		this.eventService = eventService;
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

		if (!StringUtils.hasText(newEmail)) {
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

	@GetMapping("/saleschart")
//	public ResponseEntity<List<StoreSalesAmountDto>> salesChart(@RequestParam String memberId, @RequestParam String searchDate, @RequestParam String period) {
	public ResponseEntity<List<StoreSalesAmountDto>> salesChart(Authentication authentication,
			@RequestParam String searchDate, @RequestParam String period) {

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
//	public ResponseEntity<List<StoreSalesProductTypeDto>> typeChart(@RequestParam String memberId, @RequestParam String searchDate, @RequestParam String period) {
	public ResponseEntity<List<StoreSalesProductTypeDto>> typeChart(Authentication authentication,
			@RequestParam String searchDate, @RequestParam String period) {

		System.out.println("MypageController typechart");

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

			List<StoreSalesProductTypeDto> typeChartList = mypageService.productTypeChart(memberId, searchTimestamp,
					period);

			System.out.println("MypageController typechart MypageController typechart : " + typeChartList.toString());
			return ResponseEntity.ok(typeChartList);
		} catch (Exception e) {
			System.out.println("에러났어 : " + e.getStackTrace());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@GetMapping("/titlechart")
//	public ResponseEntity<List<StoreSalesProductTitleDto>> titleChart(@RequestParam String memberId, @RequestParam String searchDate, @RequestParam String period) {
	public ResponseEntity<List<StoreSalesProductTitleDto>> titleChart(Authentication authentication,
			@RequestParam String searchDate, @RequestParam String period) {
		System.out.println("MypageController titlechart");

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

			List<StoreSalesProductTitleDto> titleChartList = mypageService.productTitleChart(memberId, searchTimestamp,
					period);

			System.out.println("MypageController typechart MypageController typechart : " + titleChartList.toString());
			return ResponseEntity.ok(titleChartList);
		} catch (Exception e) {
			System.out.println("에러났어 : " + e.getStackTrace());
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
		}
	}

	@PostMapping("/product-registration")
	public ResponseEntity<String> productRegistration(@RequestBody ProductRegistrationDto productDto)
			throws IOException  {
		
//	   public ResponseEntity<String> productRegistration(Authentication authentication, @RequestBody ProductRegistrationDto productDto) throws IOException {
		System.out.println("mypageController ProductRegistration : ");

//	      String memberId = authentication.getName();
//	      int memberAuth = memberService.findMemberAuthByMemberId(memberId);

//	      if(memberAuth != 2) {
//	         return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 필요합니다.");
//	      }

		String productTitle = productDto.getProductTitle();
		MultipartFile productImage = productDto.getProductImg();
		int priceNumber = Integer.parseInt(productDto.getPriceNumber());
		int productType = Integer.parseInt(productDto.getProductType());

		if (!StringUtils.hasText(productTitle) || productImage.isEmpty() || priceNumber <= 0 || productType <= 0) {
			return ResponseEntity.badRequest().body("필수 입력값이 누락되었습니다.");
		}

		try {

			ProductDto dto = new ProductDto();

			// 이미지 업로드 및 URL 설정
			String productImgUrl = eventService.uploadImage(productImage);

			Timestamp currentTime = new Timestamp(System.currentTimeMillis());
			DecimalFormat decimalFormat = new DecimalFormat("#,### 원");

			if (StringUtils.hasText(productImgUrl)) {
				dto.setProductImgUrl(productImgUrl);
				dto.setPriceNumber(priceNumber);
				dto.setPriceString(decimalFormat.format(priceNumber));
				dto.setCreatedAt(currentTime);
				dto.setProductEvent(0);
				dto.setProductTimeSale(0);
				dto.setProductType(productType);
				dto.setProductTitle(productTitle);

				switch (productType) {
				case 1:
					Instant expirationInstant1 = currentTime.toInstant().plus(7, ChronoUnit.DAYS);
					Timestamp expirationTimestamp1 = Timestamp.from(expirationInstant1);
					dto.setProductExpirationDate(expirationTimestamp1);
					break;
				case 2:
					Instant expirationInstant2 = currentTime.toInstant().plus(1, ChronoUnit.DAYS);
					Timestamp expirationTimestamp2 = Timestamp.from(expirationInstant2);
					dto.setProductExpirationDate(expirationTimestamp2);
					break;
				case 3:
				case 4:
					Instant expirationInstant34 = currentTime.toInstant().plus(3, ChronoUnit.DAYS);
					Timestamp expirationTimestamp34 = Timestamp.from(expirationInstant34);
					dto.setProductExpirationDate(expirationTimestamp34);
					break;
				case 5:
					Instant expirationInstant5 = currentTime.toInstant().plus(1, ChronoUnit.DAYS);
					Timestamp expirationTimestamp5 = Timestamp.from(expirationInstant5);
					dto.setProductExpirationDate(expirationTimestamp5);
					break;
				default:
					return ResponseEntity.badRequest().body("상품정보 등록 중 오류가 발생했습니다.");
				}

				System.out.println("dto : " + dto.toString());

				boolean saveSuccess = mypageService.productRegistration(dto);

				if (saveSuccess) {
					return ResponseEntity.ok("상품등록 완료");
				} else {
					return ResponseEntity.badRequest().body("상품등록 실패");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예외 에러 : " + e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다. 관리자에게 연락하세요.");

	}
}