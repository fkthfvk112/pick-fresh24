package mart.fresh.com.controller;

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
import mart.fresh.com.data.dto.ApplyManagerDto;
import mart.fresh.com.service.ApplyManagerService;
import mart.fresh.com.service.EmailService;
import mart.fresh.com.util.NaverOcrService;

@RequestMapping("/applymanager")
@RestController
public class ApplyManagerController {

	private final ApplyManagerService applyManagerService;
	private final EmailService emailService;
	private final NaverOcrService naverOcrService;

	@Autowired
	public ApplyManagerController(ApplyManagerService applyManagerService, NaverOcrService naverOcrService,
			EmailService emailService) {
		this.applyManagerService = applyManagerService;
		this.emailService = emailService;
		this.naverOcrService = naverOcrService;
	}

	@GetMapping("/apply-showList")
	public ResponseEntity<Page<ApplyManagerDto>> showApplyList(@RequestParam int page, @RequestParam int size) {
		Page<ApplyManagerDto> applyList = applyManagerService.showApplyList(page - 1, size);
		return ResponseEntity.ok(applyList);
	}

	@GetMapping("/myApply")
	public ResponseEntity<ApplyManagerDto> getMyApply(String memberId) {
		System.out.println("이거이거");
		ApplyManagerDto dto = applyManagerService.getMyApply(memberId);
	
			return ResponseEntity.ok().body(dto);
	}

	// 내가 점주를 하겠다! 라고 신청
	@PostMapping("/apply-requestapplymanager")
	public ResponseEntity<String> requestApplyManager(Authentication authentication, @RequestBody MultipartFile file)
			throws IOException {

		System.out.println("네임" + authentication.getName());
		System.out.println("파일" + file.getOriginalFilename());

		boolean isS = naverOcrService.callNaverCloudOcr(file);
		int count = applyManagerService.countByMemberMemberId(authentication.getName());
		
		if (isS && count == 0) {
			applyManagerService.requestApplyManager(authentication.getName(), file);
			return ResponseEntity.ok().body("success");
		} else if(count > 0) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("신청내역이 존재합니다.");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("사진 삽입 실패");
		}
	}

	// 입력된 아이디를 apply해줌, 즉 남의 아이디를 받아줌
	@PostMapping("apply-applymanager")
	public ResponseEntity<String> applymanager(@RequestBody Map<String, String> memberId) {
		String memberId_s = memberId.get("memberId");

		boolean isS = applyManagerService.applyManager(memberId_s);

		if (isS) {
			return ResponseEntity.ok("success");
		} else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("fail");
		}

	}

}