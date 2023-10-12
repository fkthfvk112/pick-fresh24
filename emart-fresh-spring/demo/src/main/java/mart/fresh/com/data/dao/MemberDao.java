package mart.fresh.com.data.dao;

import mart.fresh.com.data.entity.Member;

public interface MemberDao {
   
    Member login(String memberId, String memberPw);
    
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

    String getMemberName(String memberId, String memberPw);
	String getMemberEmail(String memberId, String memberPw);
    int getMemberAuth(String memberId, String memberPw);

    Member findByMemberNameAndMemberIdAndMemberEmail(String memberName, String memberId, String memberEmail);

    void updateMemberPw(Member member, String memberPw);

    Member save(Member member);

    Member findMemberId(String memberId);

	int findMemberAuthByMemberId(String memberId);

}