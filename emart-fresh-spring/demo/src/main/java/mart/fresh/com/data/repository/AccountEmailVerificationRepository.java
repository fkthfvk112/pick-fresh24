package mart.fresh.com.data.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import mart.fresh.com.data.entity.AccountEmailVerification;

public interface AccountEmailVerificationRepository extends JpaRepository<AccountEmailVerification, Long> {

	AccountEmailVerification findByMemberEmail(String memberEmail);
}
