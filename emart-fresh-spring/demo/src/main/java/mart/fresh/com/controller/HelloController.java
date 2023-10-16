package mart.fresh.com.controller;

import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/hello")
@RestController
public class HelloController {
	@GetMapping("/test")
	public ResponseEntity<String> helloTest(){
		System.out.println("helloController 테스트용입니다 " + new Date());
		
		return ResponseEntity.ok().build();
	}
}
