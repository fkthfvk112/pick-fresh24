package mart.fresh.com.data.dto;

import lombok.Data;

@Data
public class MypageDto {
	private String memberId;
    private String memberName;
    private String memberEmail;
    private int memberAuth;
    private boolean memberDel;
    private boolean isApplied;
}