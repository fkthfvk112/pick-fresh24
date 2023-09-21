package mart.fresh.com.data.dao.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.sql.Timestamp;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import mart.fresh.com.data.dao.ApplyManagerDao;
import mart.fresh.com.data.dto.ApplyManagerDto;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.data.repository.ApplyManagerRepository;

@Component
public class ApplyManagerDaoImpl implements ApplyManagerDao {

    private final ApplyManagerRepository applyManagerRepository;

    @Autowired
    public ApplyManagerDaoImpl(ApplyManagerRepository applyManagerRepository) {
        this.applyManagerRepository = applyManagerRepository;
    }

    @Override
    public int requestApplyManager(String memberId, Timestamp currentTimestamp) {
    	boolean isMemberIdExists = applyManagerRepository.existsByMember_MemberId(memberId);
    	
    	 if (!isMemberIdExists) {
    		 return applyManagerRepository.requestApplyManager(memberId, false, currentTimestamp);
    	    }
    	 
    	    return 0;
    }
    
    @Override
    public Page<ApplyManager> showApplyList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return applyManagerRepository.showApplyList(pageable);
    }

	@Override
	public int applyManager(String memberId) {
		int apply = applyManagerRepository.applyManager(memberId, true);
		int updateAuth = applyManagerRepository.updateMemberAuth(memberId, 1);
		if( apply != 1 || updateAuth != 1) { return 0; } 
		return 1;
	}
	
	
}