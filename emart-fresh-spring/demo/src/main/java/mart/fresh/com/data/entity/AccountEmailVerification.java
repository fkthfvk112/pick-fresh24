package mart.fresh.com.data.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.Data;

@Data
@Entity
@Table(name = "account_email_verification")
public class AccountEmailVerification {
	
	@Id
	@Column(name = "member_email")
    @Email
    private String memberEmail;
    
    @Column(name = "verify_code")
    private String verifyCode;
    
    @Column(name = "verify_code_expiry")
    private LocalDateTime verifyCodeExpiry;
    
}
