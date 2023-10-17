package mart.fresh.com.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dto.ApplyEmailDto;
import mart.fresh.com.service.EmailService;

@Service
public class EmailServiceImpl implements EmailService {

	private final JavaMailSender javaMailSender;

	@Autowired
    public EmailServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

	 public void sendApprovalEmail(String recipientEmail, String subject, ApplyEmailDto emailDto) {
    	 
	    	
	    	MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8"); // 인코딩을 UTF-8로 설정
	         

	           try {
	              helper.setTo(recipientEmail);
	              helper.setFrom("mailtestcjh@gmail.com");
	              helper.setSubject(subject);
	              StringBuffer htmlContent = new StringBuffer();
	              
	              htmlContent.append("<html><head>");
	              htmlContent.append("</head><body>");
	              htmlContent.append("<div style='font-family: Arial, sans-serif; padding: 90px; border: 1px solid #ccc;'>");
	              htmlContent.append("<h3><span style='color: #FFA500;'>emar24</span>");
	              htmlContent.append("<span style='color: #008000;'> Fresh</span></h3>");
	              htmlContent.append("<h1><span style='color: #FFA500;'>점주 승인</span> 요청 건</h1><br>");
	              htmlContent.append("<p>안녕하세요, <span style='font-weight: bold;'>"
	                     + "</span> 관리자님</p>");
	              htmlContent.append("<p><b>'점주 승인 요청 건'</b> 알림 메세지입니다.</p>");
	              htmlContent.append("<p>관리자 페이지에서 점주 승인요청 건을 확인해주세요.</p>");
	              htmlContent.append("<hr><br>");
	              htmlContent.append(
	            		  	"<span style='font-weight: bold; font-size: 16px;'>점주승인 요청자 정보</span><br>"
	                   +	"<span style='font-weight: bold; font-size: 16px;'> 아이디 : " + emailDto.getMemberId() +  "</span><br>"
	                   + 	"<span style='font-weight: bold; font-size: 16px;'> 이름 : " + emailDto.getMemberName() +  "</span><br>"
	                   +	"<span style='font-weight: bold; font-size: 16px;'> 지점명 : " + emailDto.getStoreName() +  "</span><br>"
	                   +	"<span style='font-weight: bold; font-size: 16px;'> 지점주소 : " + emailDto.getStoreAddress() +  "</span>");
	              htmlContent.append("<br><hr><br><br>");
	              
	              htmlContent.append("<p style='font-size: 10px;'>홈페이지에서 리스트를 확인해주세요.<br><br>");
	              htmlContent.append(
	                    "<a href='http://pick-fresh24.com/login' style='display: inline-block; padding: 15px 50px; background-color: #FFA500; color: #fff; text-decoration: none; border: 1px solid #FFA500; font-size: 11px;'><b>홈페이지 바로가기</b></a>");

	              htmlContent.append("</div></body></html>");

	              helper.setText(htmlContent.toString(), true); // true를 설정하여 HTML 형식으로 지정합니다.

	              javaMailSender.send(message);
	              System.out.println("점주신청 이메일 발송 성공");
	           } catch (MailException e) {
	        	   System.out.println("점주신청 이메일 발송 실패");
	              e.printStackTrace();
	           } catch (MessagingException e) {
	        	   System.out.println("점주신청 이메일 발송 오류");
	              e.printStackTrace();
	           }
	        }
    

	public void sendEmailFindPw(String memberEmail,String memberId, String subject, String verificationCode) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8"); // 인코딩을 UTF-8로 설정

		try {
			helper.setTo(memberEmail);
			helper.setFrom("mailtestcjh@gmail.com");
			helper.setSubject(subject);
			StringBuffer htmlContent = new StringBuffer();
			htmlContent.append("<html><head>");
			htmlContent.append("</head><body>");
			htmlContent.append("<div style='font-family: Arial, sans-serif; padding: 90px; border: 1px solid #ccc;'>");
			htmlContent.append("<h3><span style='color: #FFA500;'>emar24</span>");
			htmlContent.append("<span style='color: #008000;'> Fresh</span></h3>");
			htmlContent.append("<h1><span style='color: #FFA500;'>비밀번호 찾기</span> 이메일 인증 안내</h1><br>");
			htmlContent.append("<p>안녕하세요, <span style='font-weight: bold;'>"
					+ maskId(memberId) + "</span> 고객님</p>");
			htmlContent.append("<p><b>'비밀번호 찾기'</b>를 위해 이메일 인증을 진행합니다.</p>");
			htmlContent.append("<p>아래 발급된 이메일 인증번호를 복사하거나 직접 입력하여 인증을 완료해주세요.</p>");
			htmlContent.append("<p>개인정보 보호를 위해 인증번호는 5분 간 유효합니다.</p><br>");
			htmlContent.append("<hr><br>");
			htmlContent.append(
					"<p style='white-space: nowrap;'>&nbsp;&nbsp;인증번호 &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bold; font-size: 16px;'>"
							+ verificationCode + "</span></p>");
			htmlContent.append("<br><hr><br><br>");
			htmlContent.append(
					"<p style='font-size: 10px;'><b><span style='color: #ff0000;'>! </span>이메일 인증을 요청한 적이 없나요?</b></p><br>");
			htmlContent.append("<p style='font-size: 10px;'>타인이 고객님의 계정정보를 도용해 인증을 요청했을 수 있습니다.</p>");
			htmlContent.append("<p style='font-size: 10px;'>계정정보 유출이 의심된다면 ‘내정보’에서 비밀번호를 변경해주세요.<br><br>");
			htmlContent.append(
					"<a href='http://pick-fresh24.com/login' style='display: inline-block; padding: 15px 50px; background-color: #FFA500; color: #fff; text-decoration: none; border: 1px solid #FFA500; font-size: 11px;'><b>비밀번호 변경</b></a>");

			htmlContent.append("</div></body></html>");

			helper.setText(htmlContent.toString(), true); // true를 설정하여 HTML 형식으로 지정합니다.

			javaMailSender.send(message);
			System.out.println("비밀번호 찾기 이메일 발송 성공 - 주소: " + memberEmail + ", 인증번호 : " + verificationCode);
		} catch (MailException e) {
			System.out.println("비밀번호 찾기 이메일 발송 실패 - 주소: " + memberEmail + ", 인증번호 : " + verificationCode);
			e.printStackTrace();
		} catch (MessagingException e) {
			System.out.println("비밀번호 찾기 이메일 작성 오류 - 주소: " + memberEmail + ", 인증번호 : " + verificationCode);
			e.printStackTrace();
		}
	}

//	private String maskEmail(String email) {
//		int atIndex = email.indexOf("@");
//		if (atIndex >= 0) {
//			int startIndex = Math.max(atIndex - 4, 0); // @ 앞 4글자 또는 문자열의 시작부터
//			String maskedPart = email.substring(0, startIndex) + "****";
//			return maskedPart + email.substring(atIndex);
//		}
//		return email;
//	}
	
	private String maskId(String id) {
	    int idLength = id.length();
	    if (idLength > 5) {
	        int Index = idLength - 5;
	        String maskedPart = id.substring(0, Index) + "*****";
	        return maskedPart;
	    }
	    return id;
	}

	public void sendVerificationEmail(String memberEmail, String subject, String verificationCode) {
		MimeMessage message = javaMailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8");

		try {
			helper.setTo(memberEmail);
			helper.setFrom("mailtestcjh@gmail.com");
			helper.setSubject(subject);
			StringBuffer htmlContent = new StringBuffer();
			htmlContent.append("<html><head>");
			htmlContent.append("</head><body>");
			htmlContent.append("<div style='font-family: Arial, sans-serif; padding: 90px; border: 1px solid #ccc;'>");
			htmlContent.append("<h3><span style='color: #FFA500;'>emar24</span>");
			htmlContent.append("<span style='color: #008000;'> Fresh</span></h3>");
			htmlContent.append("<h1><span style='color: #FFA500;'>회원가입</span> 이메일 인증 안내</h1><br>");
			htmlContent.append("<p'>안녕하세요, 고객님</p>");
			htmlContent.append("<p><b>'회원가입'</b>을 위해 이메일 인증을 진행합니다.</p>");
			htmlContent.append("<p>아래 발급된 이메일 인증번호를 복사하거나 직접 입력하여 인증을 완료해주세요.</p>");
			htmlContent.append("<p>개인정보 보호를 위해 인증번호는 5분 간 유효합니다.</p><br>");
			htmlContent.append("<hr><br>");
			htmlContent.append(
					"<p style='white-space: nowrap;'>&nbsp;&nbsp;인증번호 &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bold; font-size: 16px;'>"
							+ verificationCode + "</span></p>");
			htmlContent.append("<br><hr><br><br>");
			htmlContent.append(
					"<p style='font-size: 10px;'><b><span style='color: #ff0000;'>! </span>이메일 인증을 요청한 적이 없나요?</b></p><br>");
			htmlContent.append("<p style='font-size: 10px;'>타인이 고객님의 계정정보를 도용해 인증을 요청했을 수 있습니다.</p>");
			htmlContent.append("<p style='font-size: 10px;'>계정정보 유출이 의심된다면 ‘내정보’에서 비밀번호를 변경해주세요.<br><br>");
			htmlContent.append(
					"<a href='http://pick-fresh24.com/login' style='display: inline-block; padding: 15px 50px; background-color: #FFA500; color: #fff; text-decoration: none; border: 1px solid #FFA500; font-size: 11px;'><b>비밀번호 변경</b></a>");
			htmlContent.append("</div></body></html>");

			helper.setText(htmlContent.toString(), true);

			javaMailSender.send(message);
			System.out.println("회원가입 이메일 인증 발송 성공 - 주소: " + memberEmail + ", 인증번호 : " + verificationCode);
		} catch (MailException e) {
			System.out.println("회원가입 이메일 인증 발송 실패 - 주소: " + memberEmail + ", 인증번호 : " + verificationCode);
			e.printStackTrace();
		} catch (MessagingException e) {
			System.out.println("회원가입 이메일 인증 작성 오류 - 주소: " + memberEmail + ", 인증번호 : " + verificationCode);
			e.printStackTrace();
		}
	}
	
	@Override
	public String sendEmailVerificationCode(String memberName, String recipientEmail, String subject, String verificationCode) {
    	MimeMessage message = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, "UTF-8"); // 인코딩을 UTF-8로 설정
         

           try {
              helper.setTo(recipientEmail);
              helper.setFrom("mailtestcjh@gmail.com");
              helper.setSubject(subject);
              StringBuffer htmlContent = new StringBuffer();
              
              htmlContent.append("<html><head>");
              htmlContent.append("</head><body>");
              htmlContent.append("<div style='font-family: Arial, sans-serif; padding: 90px; border: 1px solid #ccc;'>");
              htmlContent.append("<h2><span style='color: #FFA500;'>emar24</span>");
              htmlContent.append("<span style='color: #008000;'> Fresh</span></h2>");
              htmlContent.append("<h1><span style='color: #FFA500;'>이메일 변경</span> 요청 건</h1><br>");
              htmlContent.append("<p>안녕하세요, <span style='font-weight: bold;'>"
                     + memberName + "</span> 님</p>");
              htmlContent.append("<p><b>'이메일 변경 건'</b> 인증번호입니다.</p>");
              htmlContent.append("<p>인증번호 만료시간 <b>'5분'</b> 내 입력해주시기 바랍니다.</p>");
              htmlContent.append("<hr><br>");
              htmlContent.append(
                      "<p style='white-space: nowrap;'>&nbsp;&nbsp;인증번호 &nbsp;&nbsp;&nbsp;&nbsp;<span style='font-weight: bold; font-size: 16px;'>"
                            + verificationCode + "</span></p>");

              htmlContent.append("<br><hr><br><br>");
              
              htmlContent.append("<p style='font-size: 10px;'>홈페이지에서 인증번호를 입력해주세요.<br><br>");
              htmlContent.append(
                    "<a href='http://pick-fresh24.com/mypage' style='display: inline-block; padding: 15px 50px; background-color: #FFA500; color: #fff; text-decoration: none; border: 1px solid #FFA500; font-size: 11px;'><b>홈페이지 바로가기</b></a>");

              htmlContent.append("</div></body></html>");

              helper.setText(htmlContent.toString(), true); // true를 설정하여 HTML 형식으로 지정합니다.

              javaMailSender.send(message);
              System.out.println("인증번호 이메일 발송 성공");
              return "메일발송 성공";
           } catch (MailException e) {
        	   System.out.println("인증번호 이메일 발송 실패");
              e.printStackTrace();
              return "메일발송 실패";
           } catch (MessagingException e) {
        	   System.out.println("인증번호 이메일 발송 오류");
              e.printStackTrace();
              return "메일발송 오류";
           }
        }

}