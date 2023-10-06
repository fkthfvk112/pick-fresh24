package mart.fresh.com.controller;


import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import mart.fresh.com.data.dto.ApplyEmailDto;
import mart.fresh.com.data.dto.ApplyManagerDto;
import mart.fresh.com.data.entity.ApplyManager;
import mart.fresh.com.service.ApplyManagerService;
import mart.fresh.com.service.EmailService;
//import mart.fresh.com.util.NaverOcrService;


@RequestMapping("/applymanager")
@RestController
public class ApplyManagerController {

private final ApplyManagerService applyManagerService;
private final EmailService emailService;
//private final NaverOcrService naverOcrService;

	@Autowired
	public ApplyManagerController(ApplyManagerService applyManagerService,
//									NaverOcrService naverOcrService,						
									EmailService emailService) {
	    this.applyManagerService = applyManagerService;
	    this.emailService = emailService;
//	    this.naverOcrService = naverOcrService;
	}

	
	@GetMapping("/apply-showList")
	public Page<ApplyManagerDto> showApplyList(@RequestParam int page, @RequestParam int size) {
		Page<ApplyManagerDto> applyList = applyManagerService.showApplyList(page-1, size);
	    return applyList;
	}
	
	@GetMapping("/myApply")
	public ResponseEntity<ApplyManagerDto> getMyApply(String memberId) {
		System.out.println("이거이거");
		ApplyManagerDto dto =  applyManagerService.getMyApply(memberId);
		if(dto == null) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		
	    return ResponseEntity.ok().body(dto);
	}
	
	
	//내가 점주를 하겠다! 라고 신청
	@PostMapping("/apply-requestapplymanager")
	public ResponseEntity<String> requestApplyManager(Authentication authentication, @RequestBody MultipartFile file) {
		
		System.out.println("네임" + authentication.getName());
		System.out.println("파일" + file.getOriginalFilename());
	   try {
//		   String extractedText = naverOcrService.callNaverCloudOcr(file);
		   applyManagerService.requestApplyManager(authentication.getName(), file);
			return ResponseEntity.ok().body("success");
		} catch (IOException e) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("사진 삽입 실패");
		}
	}

	//입력된 아이디를 apply해줌, 즉 남의 아이디를 받아줌
	@PostMapping("apply-applymanager")
	public ResponseEntity<String>  applymanager(@RequestBody Map<String, String> memberId) {
		String memberId_s =memberId.get("memberId");

		boolean isS = applyManagerService.applyManager(memberId_s);
		
		if(isS) {
			return ResponseEntity.ok("success");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
		}
		
	}
	
}