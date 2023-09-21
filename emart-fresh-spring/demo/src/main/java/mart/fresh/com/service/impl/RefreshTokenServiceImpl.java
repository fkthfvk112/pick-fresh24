package mart.fresh.com.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.RefreshToken;
import mart.fresh.com.data.repository.RefreshTokenRepository;
import mart.fresh.com.service.RefreshTokenService;

@Service
@Transactional
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Autowired
    public RefreshTokenServiceImpl(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    public RefreshToken findByMember(Member member) {
        return refreshTokenRepository.findByMember(member);
    }

    @Override
    public RefreshToken saveRefreshToken(RefreshToken refreshToken) {
        return refreshTokenRepository.save(refreshToken);
    }

	@Override
	public boolean deleteByMemberId(String memberId) {
	    try {
	        refreshTokenRepository.deleteByMemberMemberId(memberId);
	        return true; // 삭제 성공
	    } catch (Exception e) {
	        return false; // 삭제 실패
	    }
	}


}
