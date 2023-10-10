package mart.fresh.com.data.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import mart.fresh.com.data.entity.Member;

public interface MemberRepository extends JpaRepository<Member, String> {
	// 로그인(아이디, 비번)
	Member findByMemberIdAndMemberPw(String memberId, String MemberPw);
	// 	boolean findByMemberIdAndMemberPw(String memberId, String MemberPw);
		
	// 회원 조회(아이디)
	Member findByMemberId(String memberId);

	// 아이디 찾기(이름, 이메일)
    Member findByMemberNameAndMemberEmail(String memberName, String memberEmail);

    // 비밀번호 찾기(이름, 아이디, 이메일)
    Member findByMemberNameAndMemberIdAndMemberEmail(String memberName, String memberId, String memberEmail);
    
    // 아이디, 이메일 중복확인
    int countByMemberId(String memberId);
    int countByMemberEmail(String memberEmail);

    @Query("SELECT m FROM Member m WHERE m.verifyCodeExpiry <= :currentTime")
    List<Member> findExpiredVerificationCodes(LocalDateTime currentTime);

}
