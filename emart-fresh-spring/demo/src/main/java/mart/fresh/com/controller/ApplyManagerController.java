package mart.fresh.com.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import mart.fresh.com.data.dto.ApplyEmailDto;
import mart.fresh.com.data.dto.ApplyManagerDto;
import mart.fresh.com.service.ApplyManagerService;
import mart.fresh.com.service.EmailService;


@RequestMapping("/applymanager")
@RestController
public class ApplyManagerController {

private final ApplyManagerService applyManagerService;
private final EmailService emailService;

	@Autowired
	public ApplyManagerController(ApplyManagerService applyManagerService, EmailService emailService) {
	    this.applyManagerService = applyManagerService;
	    this.emailService = emailService;
	}

	
	@PostMapping("/apply-showList")
	public Page<ApplyManagerDto> showApplyList(@RequestParam int page, @RequestParam int size) {
		Page<ApplyManagerDto> applyList = applyManagerService.showApplyList(page-1, size);
	    return applyList;
	}
	
	
	@PostMapping("/apply-requestapplymanager")
	public String requestApplyManager(@RequestBody ApplyEmailDto emailDto) {
		System.out.println("controller 확인 : " + emailDto.toString());
		
	   boolean isS = applyManagerService.requestApplyManager(emailDto.getMemberId());
	   
	   if(isS) {
		   String recipientEmail = emailDto.getMemberEmail(); // 수신자 이메일 주소 설정
           String subject = "[emart24 fresh] 점주신청 승인 건 알림메일입니다.";
     
           emailService.sendApprovalEmail(recipientEmail, subject, emailDto);
           
		   return "Success";
	   } else {
		   return "fail";
	   }
	    
	}
	
	
	@PostMapping("apply-applymanager")
	public String applymanager(String memberId) {
		
		boolean isS = applyManagerService.applyManager(memberId);
		
		if(isS) {
			return("Success");
		} else {
			return("fail");
		}
		
	}
	
}