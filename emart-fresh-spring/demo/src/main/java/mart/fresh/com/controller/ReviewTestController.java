package mart.fresh.com.controller;

import java.io.IOException;
import java.util.Date;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/review")
public class ReviewTestController {
	@PostMapping
	public ResponseEntity<String> writeReview(Authentication authentication){
		System.out.println("ReviewTestController 글 등록 " + new Date());
		return ResponseEntity.ok().body(authentication.getName()+"님의 리뷰 등록이 완료되었습니다.");
	}	
	
	@PostMapping("/hhh")
	public ResponseEntity<String> hello(){
		System.out.println("결제취소 된거 확인중 ~~~~~~~~");
		return ResponseEntity.ok(null);
	}
	
	@GetMapping("/hello")
	public String abc(Authentication auth) {
		System.out.println("ㄻㄴㅇㄻㄴㅇㄹㄻㅇㄴ");
		System.out.println("hello world!!");
		System.out.println("아이우에오" + auth.getName());
		
		return "가나다";
	}
	
	@GetMapping("/hello2")
	public String abc2(Authentication auth, String name, String age) {
		System.out.println("name: " + name);
		System.out.println("age : " + age);
		System.out.println("사용자  : " + auth.getName());
		return "아나다라마바사";
	}
	
	@GetMapping("/hi")
	public ResponseEntity<?> abc2(HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		System.out.println("abc12312312312");

        System.out.println("after this");
        
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}

