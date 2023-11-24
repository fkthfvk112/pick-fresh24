package mart.fresh.com.data.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "member")
public class Member {
    @Id
    @Column(name = "member_id")
    private String memberId;
    
    @NotEmpty
    @Column(name = "member_pw")
    private String memberPw;
    
    @Column(name = "member_name")
    @NotEmpty
    private String memberName;
    
    @Column(name = "member_email", unique = true)
    @Email
    private String memberEmail;
    
    @Column(name = "member_auth")
    private int memberAuth;
    
    @Column(name = "member_warning")
    private int memberWarning;
    
    @Column(name = "member_del")
    private boolean memberDel;
    
    @Column(name = "verify_code")
    private String verifyCode;
    
    @Column(name = "verify_code_expiry")
    private LocalDateTime verifyCodeExpiry;    
    
    @Column(name = "login_type")
    private String loginType;

    @Builder
	public Member(String memberId, @NotEmpty String memberPw, @NotEmpty String memberName, @Email String memberEmail,
			int memberAuth, int memberWarning, boolean memberDel, String verifyCode, LocalDateTime verifyCodeExpiry,
			String loginType) {
		super();
		this.memberId = memberId;
		this.memberPw = memberPw;
		this.memberName = memberName;
		this.memberEmail = memberEmail;
		this.memberAuth = memberAuth;
		this.memberWarning = memberWarning;
		this.memberDel = memberDel;
		this.verifyCode = verifyCode;
		this.verifyCodeExpiry = verifyCodeExpiry;
		this.loginType = loginType;
	}
}