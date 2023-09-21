
package mart.fresh.com.config;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import mart.fresh.com.util.JwtUtil;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtFilter extends OncePerRequestFilter {

	private final JwtUtil jwtUtil;


	@Autowired
    public JwtFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		
		boolean isError = false;
		
		String requestURI = request.getRequestURI();
		if (requestURI.startsWith("/member")) {
			System.out.println("로그인과 회원가입은 AccessToken이 필요없습니다.");
	        filterChain.doFilter(request, response);
	        return;
	    }
		
		final String authorization = request.getHeader(org.springframework.http.HttpHeaders.AUTHORIZATION);
		log.info("Authorization : {}",authorization);
		
		// accessToken 안보내거나 형식 안맞으면 컷
		if(authorization == null || !authorization.startsWith("Bearer ")) {
			System.out.println("authorization이 없거나 잘못보냈습니다.");
			filterChain.doFilter(request, response);
			return;
		}
		
		// accessToken 꺼내기
		String accessToken = authorization.split(" ")[1];
	    if(accessToken != null) { // Access 토큰이 있다면
		    if(!jwtUtil.isExpired(accessToken)) { // Access 토큰이 만료되지 않았다면
		    	String memberId = jwtUtil.getMemberIdByJwt(accessToken);
		    	// 권한 부여
		    	UsernamePasswordAuthenticationToken authenticationToken =
		    			new UsernamePasswordAuthenticationToken(memberId, null, List.of(new SimpleGrantedAuthority("우리가 정하면 됨")));
		    	SecurityContextHolder.getContext().setAuthentication(authenticationToken);
		    } else {
		    	// Access 토큰이 만료된 경우 401 Unauthorized 상태 코드를 반환
		    	System.out.println("accessToken이 만료되었습니다. 401을 반환합니다.");
		    	isError = true;		        
		        request.getRequestDispatcher("/refreshToken/401error-handle").forward(request, response);
		    }
		    
	    }
	    
	    if(!isError)filterChain.doFilter(request, response); // 인증도장
		// authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	}
}