package mart.fresh.com.service;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.web.multipart.MultipartFile;

import mart.fresh.com.data.dto.ApplyManagerDto;

public interface ApplyManagerService {
    boolean requestApplyManager(String memberId, MultipartFile file) throws IOException;
    Page<ApplyManagerDto> showApplyList(int page, int size);
    boolean applyManager(String memberId);
    ApplyManagerDto getMyApply(String memberId);
}