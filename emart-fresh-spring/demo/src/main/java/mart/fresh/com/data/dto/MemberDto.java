package mart.fresh.com.data.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {
	private String memberId;
    private String memberPw;
    private String memberName;
    private String memberEmail;
    private int memberAuth;
    private int memberWarning;
    private boolean memberDel;
    private String newPw;
    private String verifyCode;
    private Timestamp verifyCodeExpiry;

	public MemberDto(String memberId, String memberName, String memberEmail, int memberAuth, int memberWarning,
			boolean memberDel, String verifyCode, Timestamp verifyCodeExpiry) {
		super();
		this.memberId = memberId;
		this.memberName = memberName;
		this.memberEmail = memberEmail;
		this.memberAuth = memberAuth;
		this.memberWarning = memberWarning;
		this.memberDel = memberDel;
		this.verifyCode = verifyCode;
		this.verifyCodeExpiry = verifyCodeExpiry;
	}
	
	
    
}