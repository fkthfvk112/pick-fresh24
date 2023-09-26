package mart.fresh.com.service.impl;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.security.sasl.AuthenticationException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import mart.fresh.com.data.dao.MemberDao;
import mart.fresh.com.data.entity.Member;
import mart.fresh.com.data.entity.RefreshToken;
import mart.fresh.com.service.MemberService;
import mart.fresh.com.service.RefreshTokenService;
import mart.fresh.com.util.JwtUtil;

@Service
public class MemberServiceImpl implements MemberService {
   private final MemberDao memberDao;
   private final JwtUtil jwtUtil;
   private final BCryptPasswordEncoder encoder;
   
    @Autowired
    public MemberServiceImpl(MemberDao memberDao, JwtUtil jwtUtil, BCryptPasswordEncoder encoder) {
        this.memberDao = memberDao;
        this.jwtUtil = jwtUtil;
        this.encoder = encoder;

    }
    
    @Autowired
    private RefreshTokenService refreshTokenService;

    @Value("${jwt.secretKey}")
    private String secretKey;
    
    private Long expiredMs = 1*1000*30l; // 30초 임시
 
    LocalDateTime refreshTokenExpiry = LocalDateTime.now().plus(Duration.ofHours(24)); // 24시간
    
    @Override
    public Map<String, String> loginJwt(String memberId, String memberPw) {
        try {
           Member member = memberDao.getMemberById(memberId);
            if (member != null) {
               if(encoder.matches(memberPw, member.getMemberPw())) {
            	    String memberName = memberDao.getMemberName(memberId, memberPw);
            	    String memberEmail = memberDao.getMemberEmail(memberId, memberPw);
                    int memberAuth = memberDao.getMemberAuth(memberId, memberPw);
                    String jwt = JwtUtil.createJwt(memberId, memberName, memberEmail, memberAuth, jwtUtil.getSecretKey(), expiredMs);
                    
                    RefreshToken currentMember = refreshTokenService.findByMember(member);

                    String refreshToken = JwtUtil.createRefreshToken(64);
                    
                    if (currentMember != null) {
                        currentMember.setRefreshToken(refreshToken);
                        currentMember.setRefreshTokenExpiry(refreshTokenExpiry);
                        refreshTokenService.saveRefreshToken(currentMember);
                    } else {
                        // 새로운 리프레시 토큰 생성 후 저장
                        RefreshToken refreshTokenEntity = new RefreshToken();
                        refreshTokenEntity.setRefreshToken(refreshToken);
                        refreshTokenEntity.setMember(member);
                        refreshTokenEntity.setRefreshTokenExpiry(refreshTokenExpiry);
                        refreshTokenService.saveRefreshToken(refreshTokenEntity);
                    }

                    Map<String, String> response = new HashMap<>();
                    response.put("accessToken", jwt);
                    response.put("refreshToken", refreshToken);
                    
                    return response;
               } else {
                  return null;
               }
            } else {
               System.out.println("입력한 비밀번호와 DB와 일치하지 않음.");
                throw new AuthenticationException("Invalid credentials");
            }
        } catch (AuthenticationException e) {
           return null;
        }
    }
    
    @Override
    public Map<String, String> kakaoLoginJwt(String memberId) {
    	String kakaoMemberId = "[kakao]"+memberId;
    	try {
            Member member = memberDao.getMemberById(kakaoMemberId);
            if (member != null) {
            	String memberName = member.getMemberName();
            	String memberEmail = member.getMemberEmail();
                int memberAuth = member.getMemberAuth();
                String jwt = JwtUtil.createJwt(kakaoMemberId, memberName, memberEmail, memberAuth, jwtUtil.getSecretKey(), expiredMs);
                RefreshToken currentMember = refreshTokenService.findByMember(member);

                String refreshToken = JwtUtil.createRefreshToken(64);
                
                if (currentMember != null) {
                    currentMember.setRefreshToken(refreshToken);
                    currentMember.setRefreshTokenExpiry(refreshTokenExpiry);
                    refreshTokenService.saveRefreshToken(currentMember);
                } else {
                    // 새로운 리프레시 토큰 생성 후 저장
                    RefreshToken refreshTokenEntity = new RefreshToken();
                    refreshTokenEntity.setRefreshToken(refreshToken);
                    refreshTokenEntity.setMember(member);
                    refreshTokenEntity.setRefreshTokenExpiry(refreshTokenExpiry);
                    refreshTokenService.saveRefreshToken(refreshTokenEntity);
                }

                Map<String, String> response = new HashMap<>();
                response.put("accessToken", jwt);
                response.put("refreshToken", refreshToken);
                
                return response;
            } else {
            	System.out.println("DB에 회원이 없음");
                throw new AuthenticationException("Invalid credentials");
            }
    	}catch(AuthenticationException e) {
            return null;
        }
    }
    
    @Override
    public Map<String, String> naverLoginJwt(String memberId) {
    	String naverMemberId = "[naver]"+memberId;
    	try {
            Member member = memberDao.getMemberById(naverMemberId);
            if (member != null) {
            	String memberName = member.getMemberName();
            	String memberEmail = member.getMemberEmail();
                int memberAuth = member.getMemberAuth();
                String jwt = JwtUtil.createJwt(naverMemberId, memberName, memberEmail, memberAuth, jwtUtil.getSecretKey(), expiredMs);
                RefreshToken currentMember = refreshTokenService.findByMember(member);

                String refreshToken = JwtUtil.createRefreshToken(64);
                
                if (currentMember != null) {
                    currentMember.setRefreshToken(refreshToken);
                    currentMember.setRefreshTokenExpiry(refreshTokenExpiry);
                    refreshTokenService.saveRefreshToken(currentMember);
                } else {
                    // 새로운 리프레시 토큰 생성 후 저장
                    RefreshToken refreshTokenEntity = new RefreshToken();
                    refreshTokenEntity.setRefreshToken(refreshToken);
                    refreshTokenEntity.setMember(member);
                    refreshTokenEntity.setRefreshTokenExpiry(refreshTokenExpiry);
                    refreshTokenService.saveRefreshToken(refreshTokenEntity);
                }

                Map<String, String> response = new HashMap<>();
                response.put("accessToken", jwt);
                response.put("refreshToken", refreshToken);
                
                return response;
            } else {
            	System.out.println("DB에 회원이 없음");
                throw new AuthenticationException("Invalid credentials");
            }
    	}catch(AuthenticationException e) {
            return null;
        }
    }

    @Override
    public Member getMemberById(String memberId) {
        return memberDao.getMemberById(memberId);
    }

    @Override
    public void addMember(Member member) {
       String encodedPassword = encoder.encode(member.getMemberPw());
        member.setMemberPw(encodedPassword);
       
       memberDao.addMember(member);
    }
    
    @Override
    public void kakaoAddMember(String memberId, String memberName, String memberEmail) {
    	memberDao.kakaoAddMember(memberId, memberName, memberEmail);
    }
    
    @Override
    public void naverAddMember(String memberId, String memberName, String memberEmail) {
    	memberDao.naverAddMember(memberId, memberName, memberEmail);
    	
    }

    @Override
	public void localLoginType(Member member) {
		memberDao.localLoginType(member);
	}

	@Override
	public void kakaoLoginType(Member member) {
		memberDao.kakaoLoginType(member);
		
	}

	@Override
	public void naverLoginType(Member member) {
		memberDao.naverLoginType(member);
		
	}
    
   @Override
   public String findMemberId(String memberName, String memberEmail) {
        return memberDao.findMemberId(memberName, memberEmail);
   }
   
   @Override
   public Member findByMemberNameAndMemberIdAndMemberEmail(String memberName, String memberId, String memberEmail) {
      return memberDao.findByMemberNameAndMemberIdAndMemberEmail(memberName, memberId, memberEmail);
   }

   @Override
    public int memberIdCheck(String memberId) {
        return memberDao.memberIdCheck(memberId);
    }

    @Override
    public int memberEmailCheck(String memberEmail) {
        return memberDao.memberEmailCheck(memberEmail);
    }

   @Override
   public int getMemberAuth(String memberId, String memberPw) {
      return memberDao.getMemberAuth(memberId, memberPw);
   }

   @Override
   public void updateMemberPw(Member member, String memberPw) {
      memberDao.updateMemberPw(member, memberPw);
   }

   @Override
   public Member save(Member member) {
      return memberDao.save(member);
   }

   @Override
   public Member findByMemberId(String memberId) {
      return memberDao.findMemberId(memberId);
   }

}