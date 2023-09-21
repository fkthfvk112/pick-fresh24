package mart.fresh.com.service;

import mart.fresh.com.data.dto.ApplyEmailDto;

public interface EmailService {

	public void sendApprovalEmail(String recipientEmail, String subject, ApplyEmailDto emailDto);
	public void sendEmailFindPw(String memberEmail, String memberId, String subject, String verificationCode);
    public void sendVerificationEmail(String memberEmail, String subject, String verificationCode);
	public String sendEmailVerificationCode(String memberName, String recipientEmail, String subject,
			String verificationCode);

    }