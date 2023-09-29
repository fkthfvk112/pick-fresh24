package mart.fresh.com.data.dto;


import lombok.Data;

import java.sql.Timestamp;

@Data
public class ApplyManagerDto {
	private int applyManagerCount;
    private String memberId; // MyDto로 수정
    private boolean isApplied;
    private Timestamp applyDate;
    private String certifImgUrl;
}