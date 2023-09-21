package mart.fresh.com.service;

import java.util.Map;

import mart.fresh.com.data.entity.Member;

public interface MemberService {
	Map<String, String> loginJwt(String memberId, String memberPw);
	
    Member getMemberById(String memberId);
    void addMember(Member member);
    String findMemberId(String memberName, String memberEmail);
    
    int memberIdCheck(String memberId);
    int memberEmailCheck(String memberEmail);

    int getMemberAuth(String memberId, String memberPw);

    Member findByMemberNameAndMemberIdAndMemberEmail(String memberName, String memberId, String memberEmail);

    void updateMemberPw(Member member, String memberPw);

    Member save(Member member);

    Member findByMemberId(String memberId);
    
}