package mart.fresh.com.service;

import org.springframework.data.domain.Page;
import mart.fresh.com.data.dto.ApplyManagerDto;

public interface ApplyManagerService {
    boolean requestApplyManager(String memberId);
    Page<ApplyManagerDto> showApplyList(int page, int size);
    boolean applyManager(String memberId);
}