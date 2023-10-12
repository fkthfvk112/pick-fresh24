package mart.fresh.com.service;

import java.util.Map;

import mart.fresh.com.data.entity.Member;

public interface MemberService {
	Map<String, String> loginJwt(String memberId, String memberPw);
	Map<String, String> kakaoLoginJwt(String memberId);
	Map<String, String> naverLoginJwt(String memberId);
	
	void addMember(Member member);
	void kakaoAddMember(String memberId, String memberName, String memberEmail);
	void naverAddMember(String memberId, String memberName, String memberEmail);
	
	void localLoginType(Member member);
	void kakaoLoginType(Member member);
	void naverLoginType(Member member);
	
    Member getMemberById(String memberId);
    String findMemberId(String memberName, String memberEmail);
    
    int memberIdCheck(String memberId);
    int memberEmailCheck(String memberEmail);

    int getMemberAuth(String memberId, String memberPw);

    Member findByMemberNameAndMemberIdAndMemberEmail(String memberName, String memberId, String memberEmail);

    void updateMemberPw(Member member, String memberPw);

    Member save(Member member);

    Member findByMemberId(String memberId);
    
	int findMemberAuthByMemberId(String memberId);
    
}