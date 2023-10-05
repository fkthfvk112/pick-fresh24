package mart.fresh.com.controller;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import mart.fresh.com.data.dto.MemberDto;
import mart.fresh.com.data.entity.AccountEmailVerification;
import mart.fresh.com.data.entity.Cart;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.repository.AccountEmailVerificationRepository;
import mart.fresh.com.service.CartService;
import mart.fresh.com.service.EmailService;
import mart.fresh.com.service.MemberService;
import mart.fresh.com.service.RefreshTokenService;
import mart.fresh.com.util.MemberUtil;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/member")
public class MemberController {
	
	@Value("${naver.client.id}")
	private String naverClientId;

	@Value("${naver.client.secret}")
	private String naverClientSecret;
	
	
	private final MemberService memberService;
	private final EmailService emailService;
	private final CartService cartService;
	private final AccountEmailVerificationRepository accountEmailVerificationRepository;
	private final RefreshTokenService refreshTokenService;

	@Autowired
	public MemberController(MemberService memberService, EmailService emailService, CartService cartService, 
			AccountEmailVerificationRepository accountEmailVerificationRepository,
			RefreshTokenService refreshTokenService) {
		this.memberService = memberService;
		this.emailService = emailService;
		this.cartService = cartService;
		this.accountEmailVerificationRepository = accountEmailVerificationRepository;
		this.refreshTokenService = refreshTokenService;
	}

	@PostMapping("/loginJwt")
	public ResponseEntity<Map<String, Object>> loginJwt(@RequestBody Map<String, String> requestBody) {
		System.out.println("MemberController JWT로 로그인 !! " + new Date());

		String memberId = requestBody.get("memberId");
		String memberPw = requestBody.get("memberPw");
		Member member = memberService.findByMemberId(memberId);
	    if (member == null) {
	        System.out.println("[MemberController] 존재하지 않는 회원입니다.");
	        Map<String, Object> warningMessage = new HashMap<>();
            warningMessage.put("message", "존재하지 않는 회원입니다.");
	        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
	    }
		
		Map<String, String> tokens = memberService.loginJwt(memberId, memberPw);

		String loginType = memberService.findByMemberId(memberId).getLoginType();
		
		if (member.getMemberWarning() != 0 && member.getMemberWarning() % 5 == 0) {
            Map<String, Object> warningMessage = new HashMap<>();
            warningMessage.put("message", "memberWarningOver");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(warningMessage);
        }

		if (tokens != null) {
			System.out.println("토큰 발행 성공 !! " + new Date());

			Map<String, Object> responseMap = new HashMap<>();
			responseMap.put("tokens", tokens);
			responseMap.put("loginType", loginType);

			return ResponseEntity.ok().body(responseMap); // 로그인 성공 시 JWT 및 리프레시 토큰 반환
		} else {
			System.out.println("토큰 발행 실패 ㅡㅡ " + new Date());
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 실패 시 401 반환
		}
	}

	@PostMapping("/kakaoLogin")
	public ResponseEntity<?> kakaoLogin(@RequestBody Map<String, String> requestBody) {
		System.out.println("MemberController 카카오 로그인 " + new Date());

		String kakaoAccessToken = requestBody.get("access_token");

		String apiUrl = "https://kapi.kakao.com/v2/user/me";

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + kakaoAccessToken);

			int responseCode = conn.getResponseCode();
			System.out.println("카카오 응답 : " + responseCode);

			if (responseCode == 200) {
				ObjectMapper mapper = new ObjectMapper();
				JsonNode jsonNode = mapper.readTree(conn.getInputStream());

				String memberId = jsonNode.get("id").asText();
				String memberName = jsonNode.get("properties").get("nickname").asText();
				String memberEmail = jsonNode.get("kakao_account").get("email").asText();

				memberService.kakaoAddMember(memberId, memberName, memberEmail); // DB에 카카오로그인 회원 저장

				Member member = memberService.findByMemberId("[kakao]" + memberId);
				memberService.kakaoLoginType(member);

				Map<String, String> tokens = memberService.kakaoLoginJwt(memberId);

				String loginType = member.getLoginType();

				if (tokens != null) {
					System.out.println("카카오 로그인 토큰 발행 성공 " + new Date());

					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put("tokens", tokens);
					responseMap.put("loginType", loginType);

					return ResponseEntity.ok().body(responseMap); // 로그인 성공 시 JWT 및 리프레시 토큰 반환
				} else {
					System.out.println("카카오 로그인 토큰 발행 실패 " + new Date());
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 실패 시 401 반환
				}
			} else {
				System.out.println("카카오 응답 : " + responseCode);
				System.out.println("카카오 로그인 실패");
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("카카오 로그인 실패");
			}
		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@PostMapping("/naverLogin")
	public ResponseEntity<Object> naverLogin(@RequestBody Map<String, String> requestBody) {
		System.out.println("MemberController 네이버 로그인 " + new Date());

		String naverAccessToken = requestBody.get("access_token");

		String apiUrl = "https://openapi.naver.com/v1/nid/me";

		try {
			URL url = new URL(apiUrl);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Authorization", "Bearer " + naverAccessToken);

			int responseCode = conn.getResponseCode();
			System.out.println("네이버 응답 : " + responseCode);

			if (responseCode == 200) {
				ObjectMapper objectMapper = new ObjectMapper();
				JsonNode responseJson = objectMapper.readTree(conn.getInputStream());

				String memberId = responseJson.get("response").get("id").asText();
				String memberEmail = responseJson.get("response").get("email").asText();
				String memberName = responseJson.get("response").get("name").asText();

				memberService.naverAddMember(memberId, memberName, memberEmail); // DB에 네이버로그인 회원 저장

				Member member = memberService.findByMemberId("[naver]" + memberId);
				memberService.naverLoginType(member);

				Map<String, String> tokens = memberService.naverLoginJwt(memberId);

				String loginType = member.getLoginType();

				if (tokens != null) {
					System.out.println("네이버 로그인 토큰 발행 성공 " + new Date());

					Map<String, Object> responseMap = new HashMap<>();
					responseMap.put("tokens", tokens);
					responseMap.put("loginType", loginType);

					return ResponseEntity.ok().body(responseMap); // 로그인 성공 시 JWT 및 리프레시 토큰 반환
				} else {
					System.out.println("네이버 로그인 토큰 발행 실패 " + new Date());
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 실패 시 401 반환
				}
			} else {
				System.out.println("네이버 응답 : " + responseCode);
				System.out.println("네이버 로그인 실패");
				return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 실패 시 401 반환
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout1(Authentication authentication, @RequestBody Map<String, String> requestBody) {
		System.out.println("MemberController 로그아웃 " + new Date());

		String loginType = requestBody.get("loginType");
		System.out.println("Login Type: " + loginType);

		boolean result = refreshTokenService.deleteByMemberId(authentication.getName());

		RestTemplate restTemplate = new RestTemplate();

		if (loginType == null || loginType.isEmpty()) {
			System.out.println("loginType이 없습니다.");
			return ResponseEntity.badRequest().body("loginType이 없습니다.");
		} 
		
		else if (loginType.equals("local")) {
			System.out.println("MemberController 로컬 로그아웃 " + new Date());
			if (result) {
				System.out.println("로컬 로그아웃 성공");
				return ResponseEntity.ok("로컬 로그아웃 성공");
			} else {
				return ResponseEntity.badRequest().body("해당 멤버를 찾을 수 없습니다.");
			}
		} 
		
		else if (loginType.equals("kakao")) {
			System.out.println("MemberController 카카오 로그아웃 " + new Date());
			
			String kakaoAccessToken = requestBody.get("kakaoAccessToken");
			if (kakaoAccessToken != null) {
				String kakaoLogoutUrl = "https://kapi.kakao.com/v1/user/unlink";
				
				HttpHeaders headers = new HttpHeaders();
	            headers.set("Authorization", "Bearer " + kakaoAccessToken);
	            headers.setContentType(MediaType.APPLICATION_JSON);
	            
	            ResponseEntity<String> response = restTemplate.exchange(
	                    kakaoLogoutUrl, HttpMethod.POST, new HttpEntity<>(headers), String.class);
	            
				if (response.getStatusCode().is2xxSuccessful()) {
					if (result) {
						System.out.println("카카오 로그아웃 성공");
						return ResponseEntity.ok("카카오 로그아웃 성공");
					} else {
						return ResponseEntity.badRequest().body("해당 멤버를 찾을 수 없습니다.");
					}
				} else {
					System.out.println("url 보냈는데 신호가 뭔가 잘못됨");
					return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("카카오의 accessToken 값이 잘못됐습니다.");
				}
			} else {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("카카오의 accessToken이 없습니다.");
			}
		} 
		
		else if (loginType.equals("naver")) {
			System.out.println("MemberController 네이버 로그아웃 " + new Date());

			String naverAccessToken = requestBody.get("naverAccessToken");
			if (naverAccessToken != null) {
				String naverLogoutUrl = "https://nid.naver.com/oauth2.0/token?grant_type=delete"
						+ "&client_id=" + naverClientId
						+ "&client_secret=" + naverClientSecret
						+ "&access_token=" + naverAccessToken + "&service_provider=NAVER";

				ResponseEntity<String> response = restTemplate.postForEntity(naverLogoutUrl, null, String.class);

				if (response.getStatusCode().is2xxSuccessful()) {
					if (result) {
						System.out.println("네이버 로그아웃 성공");
						return ResponseEntity.ok("네이버 로그아웃 성공");
					} else {
						return ResponseEntity.badRequest().body("해당 멤버를 찾을 수 없습니다.");
					}
				} else {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("네이버의 accessToken 값이 잘못됐습니다.");
				}
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("네이버의 accessToken이 없습니다.");
			}
		} else {
			return ResponseEntity.badRequest().body("알수없는 로그인타입입니다.");
		}
	}

	@PostMapping("/add")
	public ResponseEntity<String> addMember(@RequestBody @Valid Member member) {
		System.out.println("MemberController 회원가입 " + new Date());

		if (!MemberUtil.isValidMemberInfo(member)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		try {
			memberService.addMember(member);
			memberService.localLoginType(member);
			
			Cart cart = new Cart();
	        cart.setMember(member);
	        cartService.saveCart(cart);

			return new ResponseEntity<>(HttpStatus.CREATED);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/checkVerifyEmail")
	public ResponseEntity<String> checkVerifyEmail(@RequestBody Map<String, String> requestBody) {
		String memberEmail = requestBody.get("memberEmail");

		System.out.println("MemberController 회원가입 시 이메일 인증 " + new Date());

		AccountEmailVerification accountEmailVerification = new AccountEmailVerification();
		String verificationCode = MemberUtil.generateVerificationCode();
		LocalDateTime currentTime = LocalDateTime.now();
		LocalDateTime expiryTime = currentTime.plusMinutes(5); // 5분 후 만료

		accountEmailVerification.setMemberEmail(memberEmail);
		accountEmailVerification.setVerifyCode(verificationCode);
		accountEmailVerification.setVerifyCodeExpiry(expiryTime);

		accountEmailVerificationRepository.save(accountEmailVerification);

		if (memberEmail != null) {

			String subject = "[emart24 fresh] 회원가입 이메일 인증";
			try {
				emailService.sendVerificationEmail(memberEmail, subject, verificationCode);
				return new ResponseEntity<>(HttpStatus.OK);
			} catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/checkAccountEmailVerification")
	public ResponseEntity<String> checkAccountEmailVerification(@RequestBody Map<String, String> requestBody) {
		System.out.println("MemberController 인증번호 확인 " + new Date());
		String memberEmail = requestBody.get("memberEmail");
		String verificationCode = requestBody.get("verificationCode");
		String verificationCodeDB = accountEmailVerificationRepository.findByMemberEmail(memberEmail).getVerifyCode();

		if (verificationCode.equals(verificationCodeDB)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/findId")
	public ResponseEntity<String> findMemberId(@RequestBody Map<String, String> requestBody) {
		String memberName = requestBody.get("memberName");
		String memberEmail = requestBody.get("memberEmail");

		System.out.println("MemberController 아이디 찾기 " + new Date());
		String memberId = memberService.findMemberId(memberName, memberEmail);
		if (memberId != null) {
			return new ResponseEntity<>(memberId, HttpStatus.OK);
		} else {
			System.out.println("찾으려는 아이디가 없음");
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/findPw")
	public ResponseEntity<String> findMemberPw(@RequestBody Map<String, String> requestBody) {
		System.out.println("MemberController 비밀번호 찾기 " + new Date());

		String memberName = requestBody.get("memberName");
		String memberId = requestBody.get("memberId");
		String memberEmail = requestBody.get("memberEmail");

		Member foundMember = memberService.findByMemberNameAndMemberIdAndMemberEmail(memberName, memberId, memberEmail);

		if (foundMember != null) {
			LocalDateTime currentTime = LocalDateTime.now();

			String verificationCode = MemberUtil.generateVerificationCode();
			LocalDateTime expiryTime = currentTime.plusMinutes(5); // 5분 후 만료

			String subject = "[emart24 fresh] 비밀번호 찾기";

			emailService.sendEmailFindPw(foundMember.getMemberEmail(), foundMember.getMemberId(), subject,
					verificationCode);

			foundMember.setVerifyCode(verificationCode);
			foundMember.setVerifyCodeExpiry(expiryTime);

			// 인증 정보 저장
			foundMember = memberService.save(foundMember);

			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/checkVerificationCode")
	public ResponseEntity<String> checkVerificationCode(@RequestBody Map<String, String> requestBody) {
		String memberId = requestBody.get("memberId");
		String verificationCode = requestBody.get("verificationCode");
		System.out.println("MemberController 인증번호 확인 " + new Date());
		String verificationCodeDB = memberService.findByMemberId(memberId).getVerifyCode();

		if (verificationCode.equals(verificationCodeDB)) {
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
	}

	@PostMapping("/updatePw")
	public ResponseEntity<String> updateMemberPassword(@RequestBody Map<String, String> requestBody) {
		String memberId = requestBody.get("memberId");
		String memberPw = requestBody.get("memberPw");

		// memberId로 회원 조회
		Member member = memberService.findByMemberId(memberId);

		if (member != null) {
			// 회원이 존재하면 비밀번호 업데이트
			memberService.updateMemberPw(member, memberPw);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/idCheck")
	public ResponseEntity<Integer> idcheck(@RequestParam("memberId") String memberId) {
		System.out.println("MemberController 아이디 중복 확인 " + new Date());
		int result = memberService.memberIdCheck(memberId);
		if (result == 0) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}

	@GetMapping("/emailCheck")
	public ResponseEntity<Integer> emailcheck(@RequestParam("memberEmail") String memberEmail) {
		System.out.println("MemberController 이메일 중복 확인 " + new Date());
		int result = memberService.memberEmailCheck(memberEmail);
		if (result == 0) {
			return ResponseEntity.ok(result);
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(result);
		}
	}

	@GetMapping("/isValidMember")
	public String isValidUser(Authentication auth) {
		return "valid";
	}

	@GetMapping("/getMemberInfo")
	public ResponseEntity<Member> getMemberInfoById(String memberId) {
		Member member = memberService.findByMemberId(memberId);
		if(member != null) {
			return ResponseEntity.ok().body(member);
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(member);
	}
}