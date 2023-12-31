package mart.fresh.com.data.dao;

import java.sql.Timestamp;
import org.springframework.data.domain.Page;

import mart.fresh.com.data.dto.ApplyManagerDto;
import mart.fresh.com.data.entity.ApplyManager;

public interface ApplyManagerDao {
    int requestApplyManager(String memberId, String imgUrl, Timestamp currentTimestamp);
    Page<ApplyManager> showApplyList(int page, int size);
    int applyManager(String memberId);
    ApplyManager getMyApply(String memberId);
	int countByMemberMemberId(String memberId);
}