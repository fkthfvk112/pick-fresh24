package mart.fresh.com.util;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.Random;
import java.util.regex.Pattern;

import mart.fresh.com.data.entity.Member;

public class MemberUtil {
	
	// 인증번호 생성 (임의로 6자리 숫자로 생성하도록 설정)
    public static String generateVerificationCode() {
       Random random = new Random();
       int code = 100000 + random.nextInt(900000);
       return String.valueOf(code);
    }
    
    // 이메일 인증 토큰 생성
    public static String createToken() {
		SecureRandom randomString = new SecureRandom();
		byte[] bytes = new byte[32];
		randomString.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
   
    // 회원가입 시 형식 검사
   public static boolean isValidMemberInfo(Member member) {
     // 아이디, 비밀번호, 이름, 이메일이 비어있을경우 false
     if (member.getMemberId().trim().isEmpty() || member.getMemberPw().trim().isEmpty() || member.getMemberName().trim().isEmpty()
           || member.getMemberEmail().trim().isEmpty()) {
        return false;
     }
     // Regular Expression (정규 표현) > Regex
     // 아이디 유효성 검사
     String idRegex = "^(?=.*[a-zA-Z])(?=.*[0-9])[a-zA-Z0-9]{8,12}$";
     if (!Pattern.matches(idRegex, member.getMemberId().trim())) {
        return false; // 아이디가 유효성 검사를 통과하지 않을 경우 false를 반환
     }

     // 비밀번호 조합 검사
     // 최소 9자리 이상, 하나 이상의 영문자+숫자+특문(특수문자는 ~!@#$%^&* 중 하나)
     String pwRegex = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*])[A-Za-z\\d@$!%*#?&]{8,16}$";
     if (!Pattern.matches(pwRegex, member.getMemberPw().trim())) {
        return false; // 비밀번호가 조합 조건을 만족하지 않을 경우 false를 반환
     }

     // 아이디, 비밀번호가 같으면 안되는 조건 추가
     if (member.getMemberId().equals(member.getMemberPw())) {
        return false; // 아이디와 비밀번호가 같으면 false를 반환
     }

     // 이름 형식 검사
     String nameRegex = "^[가-힣]{2,5}$";
     if (!Pattern.matches(nameRegex, member.getMemberName().trim())) {
        return false; // 이름이 유효성 검사를 통과하지 않을 경우 false를 반환
     }

     // 이메일 형식 검사
     String emailRegex = "^[a-zA-Z0-9]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,4}$";
     if (!Pattern.matches(emailRegex, member.getMemberEmail().trim())) {
        return false; // 유효하지 않은 이메일 형식일 경우 false를 반환
     }
     return true;
  }

}
