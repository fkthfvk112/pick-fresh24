package mart.fresh.com.data.dao.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import mart.fresh.com.data.dao.MemberDao;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.repository.MemberRepository;

@Component
public class MemberDaoImpl implements MemberDao {
    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder encoder;


    @Autowired
    public MemberDaoImpl(MemberRepository memberRepository, BCryptPasswordEncoder encoder) {
        this.memberRepository = memberRepository;
        this.encoder = encoder;
    }
    
   @Override
   public Member login(String memberId, String memberPw) {
        return memberRepository.findByMemberIdAndMemberPw(memberId, memberPw);
   }
    
    @Override
    public Member getMemberById(String memberId) {
        return memberRepository.findById(memberId).orElse(null);
    }

    @Override
    public void addMember(Member member) {
       System.out.println("----회원가입 멤버 추가---- ");
        memberRepository.save(member);
    }
    
    @Override
    public void kakaoAddMember(String memberId, String memberName, String memberEmail) {
    	
    	String kakaoMemberId = "[kakao]"+memberId;
    	String kakaoMemberName = "[kakao]"+memberName;
    	String kakaoMemberEmail = "[kakao]"+memberEmail;

    	Member member = new Member();
        member.setMemberId(kakaoMemberId);
        member.setMemberPw(null);
        member.setMemberName(kakaoMemberName);
        member.setMemberEmail(kakaoMemberEmail);
        
        memberRepository.save(member);    	
    }
    
    @Override
    public void naverAddMember(String memberId, String memberName, String memberEmail) {
    	String naverMemberId = "[naver]"+memberId;
    	String naverMemberName = "[naver]"+memberName;
    	String naverMemberEmail = "[naver]"+memberEmail;

    	Member member = new Member();
        member.setMemberId(naverMemberId);
        member.setMemberPw(null);
        member.setMemberName(naverMemberName);
        member.setMemberEmail(naverMemberEmail);
        
        memberRepository.save(member); 
    	
    }
    
	@Override
	public void localLoginType(Member member) {
		member.setLoginType("local");
		memberRepository.save(member);
	}
	
	@Override
	public void kakaoLoginType(Member member) {
		member.setLoginType("kakao");
		memberRepository.save(member);
	}
	
	@Override
	public void naverLoginType(Member member) {
		member.setLoginType("naver");
		memberRepository.save(member);
	}
    
    @Override
    public String findMemberId(String memberName, String memberEmail) {
        Member member = memberRepository.findByMemberNameAndMemberEmail(memberName, memberEmail);
        if (member != null) {
            return member.getMemberId();
        } else {
            return null;
        }
    }

   @Override
   public Member findByMemberNameAndMemberIdAndMemberEmail(String memberName, String memberId, String memberEmail) {
        return memberRepository.findByMemberNameAndMemberIdAndMemberEmail(memberName, memberId, memberEmail);
   }
    
    @Override
    public int memberIdCheck(String memberId) {
        return memberRepository.countByMemberId(memberId) > 0 ? 1 : 0;
    }

    @Override
    public int memberEmailCheck(String memberEmail) {
        return memberRepository.countByMemberEmail(memberEmail) > 0 ? 1 : 0;
    }
    
    @Override
    public String getMemberName(String memberId, String memberPw) {
    	Member member = memberRepository.findByMemberId(memberId);
        if (member != null && encoder.matches(memberPw, member.getMemberPw())) {
            return member.getMemberName();
        }
        return null; // 인증 실패
    }


    @Override
    public String getMemberEmail(String memberId, String memberPw) {
    	Member member = memberRepository.findByMemberId(memberId);
        if (member != null && encoder.matches(memberPw, member.getMemberPw())) {
            return member.getMemberEmail();
        }
        return null; // 인증 실패
    }
    
    @Override
    public int getMemberAuth(String memberId, String memberPw) {
        Member member = memberRepository.findByMemberId(memberId);
        if (member != null && encoder.matches(memberPw, member.getMemberPw())) {
            return member.getMemberAuth();
        }
        return -1; // 인증 실패
    }


   @Override
   public void updateMemberPw(Member member, String memberPw) {
       String encodedPw = encoder.encode(memberPw);
      member.setMemberPw(encodedPw);
        memberRepository.save(member);
   }


   @Override
   public Member save(Member member) {
      return memberRepository.save(member);
   }


   @Override
   public Member findMemberId(String memberId) {
      return memberRepository.findByMemberId(memberId);
   }




}