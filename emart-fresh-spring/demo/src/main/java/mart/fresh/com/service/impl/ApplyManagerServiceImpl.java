package mart.fresh.com.service.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import mart.fresh.com.data.dao.ApplyManagerDao;
import mart.fresh.com.data.dto.ApplyManagerDto;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.data.repository.ApplyManagerRepository;
import mart.fresh.com.service.ApplyManagerService;

@Service
public class ApplyManagerServiceImpl implements ApplyManagerService {
    private final ApplyManagerDao applyManagerDao;
    private final ApplyManagerRepository applyManagerRepository;

    @Autowired
    public ApplyManagerServiceImpl(ApplyManagerDao applyManagerDao, ApplyManagerRepository applyManagerRepository) {
        this.applyManagerDao = applyManagerDao;
        this.applyManagerRepository = applyManagerRepository;
    }

    @Override
    public boolean requestApplyManager(String memberId) {
        Timestamp currentTimestamp = new Timestamp(System.currentTimeMillis());
        return applyManagerDao.requestApplyManager(memberId, currentTimestamp) > 0;
    }
    
    @Override
    public Page<ApplyManagerDto> showApplyList(int page, int size) {
    	
        Page<ApplyManager> applyManagerPage = applyManagerDao.showApplyList(page, size);
        
        Page<ApplyManagerDto> applyManagerDtoPage = applyManagerPage.map(applyManager -> {
            ApplyManagerDto dto = new ApplyManagerDto();
            dto.setMemberId(applyManager.getMember().getMemberId());
            dto.setApplied(applyManager.isApplied());
            dto.setApplyDate(applyManager.getApplyDate());
            
            int applyManagerCount = applyManagerRepository.applyManagerCount();
            dto.setApplyManagerCount(applyManagerCount);
            return dto;
        });

        return applyManagerDtoPage;
    	
    	
    }

	@Override
	public boolean applyManager(String memberId) {
		return applyManagerDao.applyManager(memberId)>0?true:false;
	}
    
}