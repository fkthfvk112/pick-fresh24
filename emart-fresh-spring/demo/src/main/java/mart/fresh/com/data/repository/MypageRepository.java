package mart.fresh.com.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.data.entity.Member;

public interface MypageRepository extends JpaRepository<Member, String> {

	@Query("SELECT am " + "FROM ApplyManager am " + "WHERE am.member.memberId = :memberId")
	ApplyManager getMemberAndIsAppliedByMemberId(@Param("memberId") String memberId);

	@Transactional
	@Modifying
	@Query("UPDATE Member m " + "SET m.memberPw = :newPw " + "WHERE m.memberId = :memberId AND m.memberPw = :memberPw")
	int changePassword(@Param("memberId") String memberId, @Param("memberPw") String memberPw,
			@Param("newPw") String newPw);

	@Query("SELECT COUNT(m) " + "FROM Member m " + "WHERE m.memberEmail = :newEmail")
	int checkEmail(String newEmail);

	Member findMemberByMemberId(String memberId);

	Member findByMemberIdAndVerifyCode(String memberId, String verificationCode);

}