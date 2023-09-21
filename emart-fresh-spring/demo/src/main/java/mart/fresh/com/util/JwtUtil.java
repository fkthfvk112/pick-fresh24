package mart.fresh.com.util;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.security.Key;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${jwt.secretKey}")
    private String secretKey;

	
	public static String createJwt(String memberId, String memberName, String memberEmail, int memberAuth, String secretKey, Long expiredMs) {
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		
		if(expiredMs <=0) {
			throw new RuntimeException("만료시간이 0보다 커야합니다.");
		}
		
		return Jwts.builder()
	            .claim("memberId", memberId)
	            .claim("memberName", memberName)
	            .claim("memberEmail", memberEmail)
	            .claim("memberAuth", memberAuth)
				.signWith(signatureAlgorithm, secretKey)
				.setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + expiredMs))
				.compact();
	}
	
	public static String createRefreshToken(int lenght) {
		SecureRandom randomString = new SecureRandom();
		byte[] bytes = new byte[lenght];
		randomString.nextBytes(bytes);
		return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
	}
	
	public String getMemberIdByJwt(String token) {
	    Claims claims = Jwts.parserBuilder()
	        .setSigningKey(secretKey)
	        .build()
	        .parseClaimsJws(token)
	        .getBody();
	    return claims.get("memberId", String.class);
	}
	
	public int getMemberAuthByJwt(String token) {
	    Claims claims = Jwts.parserBuilder()
	        .setSigningKey(secretKey)
	        .build()
	        .parseClaimsJws(token)
	        .getBody();
	    return claims.get("memberAuth", Integer.class);
	}

	
	public String getSecretKey() {
        return secretKey;
    }
	
	public boolean isExpired(String token) {
//	    return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
//	            .getBody().getExpiration().before(new Date());	// 유효한 경우 false, 만료 true
		try {
			Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token)
            .getBody().getExpiration().before(new Date());
			
			return false;
		}
		catch (Exception e) {
			System.out.println("JwtUtil 만료된 토큰입니다.");
			return true;
		}
	}
	
	public boolean isRefreshTokenExpired(String refreshToken, LocalDateTime refreshTokenExpiry) {
        if (refreshToken == null || refreshTokenExpiry == null) {
            return false;
        }
        LocalDateTime currentTime = LocalDateTime.now();
        return refreshTokenExpiry.isAfter(currentTime);	// 유효한 경우 true, 만료 false
    }
	
}