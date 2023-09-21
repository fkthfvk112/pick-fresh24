package mart.fresh.com.controller;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestBody;

import mart.fresh.com.data.entity.RefreshToken;
import mart.fresh.com.data.repository.RefreshTokenRepository;

import mart.fresh.com.util.JwtUtil;

@RestController
@RequestMapping("/refreshToken")
public class RefreshTokenController {
	private final JwtUtil jwtUtil;
	private final RefreshTokenRepository refreshTokenRepository;
    
    @Autowired
    public RefreshTokenController(JwtUtil jwtUtil, RefreshTokenRepository refreshTokenRepository) {
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
    }
    
    @PostMapping("/newAccessToken")
    public ResponseEntity<?> newAccessToken(@RequestBody  Map<String, String> requestToken){
    	System.out.println("RefreshTokenController AccessToken 재발급 " + new Date());
    	System.out.println("newAccessToken : " + requestToken);
    	try {
            String refreshToken = requestToken.get("refreshToken");
            // Refresh Token을 사용하여 새로운 Access Token 발급
            RefreshToken refreshTokenObj = refreshTokenRepository.findByRefreshToken(refreshToken);
           
            if (refreshTokenObj != null) {
                String memberId = refreshTokenObj.getMember().getMemberId();
                String memberName = refreshTokenObj.getMember().getMemberName();
                String memberEmail = refreshTokenObj.getMember().getMemberEmail();
                int memberAuth = refreshTokenObj.getMember().getMemberAuth();
                
                Long expiredMs = 30 * 60 * 1000L;
                
                // 새로운 액세스 토큰 생성
                String newAccessToken = JwtUtil.createJwt(memberId, memberName, memberEmail, memberAuth, jwtUtil.getSecretKey(), expiredMs);
                
                // 새로운 액세스 토큰을 클라이언트에게 반환
                Map<String, String> response = new HashMap<>();
                response.put("newAccessToken", newAccessToken);

                return ResponseEntity.ok().body(response);
            } else { // Refresh Token이 유효하지 않은 경우 401 반환
            	return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
            		    .body("Refresh Token이 유효하지 않습니다.");
            	}
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
	@GetMapping("/401error-handle")
	public ResponseEntity<?> catch401ErrorHandler(HttpServletRequest req, HttpServletResponse res) throws IOException {
    	System.out.println("RefreshTokenController catch401ErrorHandler " + new Date());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}