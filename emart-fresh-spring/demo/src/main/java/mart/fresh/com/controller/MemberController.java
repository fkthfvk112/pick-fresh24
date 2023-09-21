package mart.fresh.com.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.security.core.Authentication;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.SessionAttributes;

import com.fasterxml.jackson.databind.ObjectMapper;

import mart.fresh.com.config.JwtFilter;
import mart.fresh.com.data.entity.AccountEmailVerification;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.repository.AccountEmailVerificationRepository;
import mart.fresh.com.service.EmailService;
import mart.fresh.com.service.MemberService;
import mart.fresh.com.service.RefreshTokenService;
import mart.fresh.com.util.MemberUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;
    private final EmailService emailService;
    private final AccountEmailVerificationRepository accountEmailVerificationRepository;
    private final RefreshTokenService refreshTokenService;

    @Autowired
    public MemberController(MemberService memberService, EmailService emailService,
    		AccountEmailVerificationRepository accountEmailVerificationRepository, RefreshTokenService refreshTokenService) {
        this.memberService = memberService;
        this.emailService = emailService;
        this.accountEmailVerificationRepository = accountEmailVerificationRepository;
        this.refreshTokenService = refreshTokenService;
    }
    
    @PostMapping("/loginJwt")
    public ResponseEntity<Map<String, String>> loginJwt(@RequestBody Map<String, String> requestBody) {
       System.out.println("MemberController JWT로 로그인 !! " + new Date());

        String memberId = requestBody.get("memberId");
        String memberPw = requestBody.get("memberPw");
        Map<String, String> tokens = memberService.loginJwt(memberId, memberPw);
                
        if (tokens != null) {
           System.out.println("토큰 발행 성공 !! " + new Date());
           
           return ResponseEntity.ok().body(tokens); // 로그인 성공 시 JWT 및 리프레시 토큰 반환
        } else {
           System.out.println("토큰 발행 실패 ㅡㅡ " + new Date());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build(); // 로그인 실패 시 401 반환
        }
    } 
    
    @PostMapping("/add")                  // @Valid 어노테이션을 사용하여 입력된 Member 객체를 검증
    public ResponseEntity<String> addMember(@RequestBody @Valid Member member) {
        System.out.println("MemberController 회원가입 " + new Date());
        
        if (!MemberUtil.isValidMemberInfo(member)) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        try {
            memberService.addMember(member);
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
    public ResponseEntity<String> checkAccountEmailVerification(@RequestBody Map<String, String> requestBody){
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

            emailService.sendEmailFindPw(foundMember.getMemberEmail(), foundMember.getMemberId(), subject, verificationCode);

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
    public ResponseEntity<String> checkVerificationCode(@RequestBody Map<String, String> requestBody){
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
    
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam("memberId") String memberId) {
        System.out.println("MemberController 로그아웃 " + new Date());

        boolean result = refreshTokenService.deleteByMemberId(memberId);

        if (result) {
            // 로그아웃 성공
            return ResponseEntity.ok().build();
        } else {
            // 로그아웃 실패
            return ResponseEntity.badRequest().build();
        }
    }
    
    @GetMapping("/isValidMember")
    public String isValidUser(Authentication auth) {
    	return "valid";
    }

}