package mart.fresh.com.service;

import java.util.Optional;

import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.RefreshToken;

public interface RefreshTokenService {

	RefreshToken findByMember(Member member);
    
    // 리프레시 토큰 저장
    RefreshToken saveRefreshToken(RefreshToken refreshToken);

    boolean deleteByMemberId(String memberId);
}