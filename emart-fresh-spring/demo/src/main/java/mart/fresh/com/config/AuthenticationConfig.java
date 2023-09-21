package mart.fresh.com.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


import lombok.RequiredArgsConstructor;
import mart.fresh.com.util.JwtUtil;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class AuthenticationConfig {
	
	private final JwtUtil jwtUtil;

	
	@Value("${jwt.secretKey}")
	private String secretKey;
	
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
		http
		.httpBasic().disable()
		.csrf().disable()
		.addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
		.authorizeHttpRequests()
    	.requestMatchers(new AntPathRequestMatcher("/member/**")).permitAll()
    	.requestMatchers(new AntPathRequestMatcher("/refreshToken/**")).permitAll()
    	.requestMatchers(new AntPathRequestMatcher("/**")).permitAll()
    	//.requestMatchers(new AntPathRequestMatcher("/**")).permitAll();
    	//.requestMatchers(new AntPathRequestMatcher("/review/**")).permitAll()
        .anyRequest().authenticated(); 	// 이외 모든 요청은 인증필요
		
		return http.build();
	}
}