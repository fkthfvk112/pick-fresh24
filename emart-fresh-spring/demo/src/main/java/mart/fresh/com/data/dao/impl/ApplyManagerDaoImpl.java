package mart.fresh.com.data.dao.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import mart.fresh.com.data.dao.ApplyManagerDao;
import mart.fresh.com.data.dto.ApplyManagerDto;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.repository.ApplyManagerRepository;
import mart.fresh.com.data.repository.MemberRepository;

@Component
public class ApplyManagerDaoImpl implements ApplyManagerDao {

    private final ApplyManagerRepository applyManagerRepository;
    private final MemberRepository memberRepository;

    @Autowired
    public ApplyManagerDaoImpl(ApplyManagerRepository applyManagerRepository, MemberRepository memberRepository) {
        this.applyManagerRepository = applyManagerRepository;
        this.memberRepository = memberRepository;
    }

    @Override
    public int requestApplyManager(String memberId, String imgUrl, Timestamp currentTimestamp) {    	
    	Optional<Member> op_member= memberRepository.findById(memberId);
    	if(op_member.isPresent()) {
    		Member member = op_member.get();
        	ApplyManager applyManagerEntity = new ApplyManager();
        	applyManagerEntity.setApplied(false);
        	applyManagerEntity.setApplyDate(currentTimestamp);
        	applyManagerEntity.setMember(member);
        	applyManagerEntity.setCertifImgUrl(imgUrl);
        	applyManagerRepository.save(applyManagerEntity);
        	
        	return 1;
    	}else {
    		throw new ResponseStatusException(HttpStatus.FORBIDDEN); 
    	}
    }
    
    @Override
    public Page<ApplyManager> showApplyList(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return applyManagerRepository.showApplyList(pageable);
    }

    @Transactional
	@Override
	public int applyManager(String memberId) {
    	System.out.println("아이디" + memberId);
		int apply = applyManagerRepository.applyManager(memberId, true);
		int updateAuth = applyManagerRepository.updateMemberAuth(memberId, 1);
		if( apply != 1 || updateAuth != 1) { return 0; }
		
		return 1;
	}
	
	
}