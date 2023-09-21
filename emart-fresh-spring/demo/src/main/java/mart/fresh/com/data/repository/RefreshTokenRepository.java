package mart.fresh.com.data.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import jakarta.transaction.Transactional;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.RefreshToken;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Integer> {
	
    RefreshToken findByMember(Member member);

	RefreshToken findByRefreshToken(String refreshToken);
	
	@Transactional
    void deleteByMemberMemberId(String memberId);

	@Query("SELECT rt FROM RefreshToken rt WHERE rt.refreshTokenExpiry <= :currentTime")
	List<RefreshToken> findExpiredRefreshTokens(LocalDateTime currentTime);

}
