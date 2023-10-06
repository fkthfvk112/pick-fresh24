package mart.fresh.com.data.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import mart.fresh.com.data.entity.ApplyManager;
import java.sql.Timestamp;

public interface ApplyManagerRepository extends JpaRepository<ApplyManager, Integer> {

	boolean existsByMember_MemberId(String memberId);

	@Transactional
	@Modifying
	@Query(value = "INSERT INTO apply_manager (member_id, is_applied, apply_date) "
			+ "SELECT :memberId, :isApplied, :currentTimestamp", nativeQuery = true)
	int requestApplyManager(@Param("memberId") String memberId, @Param("isApplied") boolean isApplied,
			@Param("currentTimestamp") Timestamp currentTimestamp);

	@Query("SELECT am " + "FROM ApplyManager AS am " + "WHERE am.isApplied = FALSE " + "ORDER BY am.applyDate DESC")
	Page<ApplyManager> showApplyList(Pageable pageable);

	@Transactional
	@Modifying
	@Query("UPDATE ApplyManager am " + "SET am.isApplied = :isApplied " + "WHERE am.member.memberId = :memberId")
	int applyManager(@Param("memberId") String memberId, @Param("isApplied") boolean isApplied);

	@Transactional
	@Modifying
	@Query("UPDATE Member m " + "SET m.memberAuth = :memberAuth WHERE m.memberId = :memberId")
	int updateMemberAuth(@Param("memberId") String memberId, @Param("memberAuth") int memberAuth);

	@Query("SELECT COUNT(am) " + "FROM ApplyManager am")
	int applyManagerCount();

	@Query("SELECT am "
			+ "FROM ApplyManager am "
			+ "WHERE am.member.memberId = :memberId ")
	ApplyManager findByMemberId(@Param("memberId") String memberId);
}