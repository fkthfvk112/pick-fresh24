package mart.fresh.com.controller;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.util.StringUtils;
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

import mart.fresh.com.data.dto.EventDto;
import mart.fresh.com.data.dto.EventUpdateDto;
import mart.fresh.com.service.EventService;
import mart.fresh.com.service.MemberService;

@RequestMapping("/event")
@RestController
public class EventController {
	private final EventService eventService;
	private final MemberService memberService;

	@Autowired
	public EventController(EventService eventService, MemberService memberService) {
		this.eventService = eventService;
		this.memberService = memberService;
	}

	@GetMapping("/event-list")
	public ResponseEntity<Page<EventDto>> eventList(@RequestParam int page, @RequestParam int size) {
		Page<EventDto> eventList = eventService.eventList(page - 1, size);
		return ResponseEntity.ok(eventList);
	}

	@GetMapping("/now-event-list")
	public ResponseEntity<List<Map<String, Object>>> nowEventList() {
		List<EventDto> eventList = eventService.nowEventList();

		List<Map<String, Object>> nowEventList = eventList.stream().map(eventDto -> {
			Map<String, Object> eventMap = new HashMap<>();
			eventMap.put("eventId", eventDto.getEventId());
			eventMap.put("eventBannerImage", eventDto.getEventBannerImage());
			return eventMap;
		}).collect(Collectors.toList());

		return ResponseEntity.ok(nowEventList);
	}

	@GetMapping("/detail")
	public ResponseEntity<EventDto> eventList(@RequestParam int eventId) {
		EventDto eventDetail = eventService.eventDetail(eventId);
		return ResponseEntity.ok(eventDetail);
	}

	@PostMapping("/event-update")

	public ResponseEntity<String> eventUpdate(Authentication authentication, @RequestBody EventUpdateDto eventDto)
			throws IOException {
		System.out.println("EventController eventUpdate1 : " + eventDto.toString());

		String eventTitle = eventDto.getEventTitle();
		MultipartFile eventBannerImage = eventDto.getEventBannerImage();
		MultipartFile eventDetailImage = eventDto.getEventDetailImage();
		String eventStartDate = eventDto.getEventStartDate();
		String eventEndDate = eventDto.getEventEndDate();

		if (!StringUtils.hasText(eventTitle) || eventBannerImage.isEmpty() || eventDetailImage.isEmpty()
				|| !StringUtils.hasText(eventStartDate) || !StringUtils.hasText(eventEndDate)) {
			return ResponseEntity.badRequest().body("필수 입력값이 누락되었습니다.");
		}

		int memberAuth = memberService.findMemberAuthByMemberId(authentication.getName());

		if (memberAuth != 2) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("관리자 권한이 필요합니다.");
		}

		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date startParsedDate = dateFormat.parse(eventStartDate);
			Date endParsedDate = dateFormat.parse(eventEndDate);

			Timestamp startTimestamp = new Timestamp(startParsedDate.getTime());
			Timestamp endTimestamp = new Timestamp(endParsedDate.getTime());

			EventDto dto = new EventDto();
			dto.setEventTitle(eventTitle);

			// 이미지 업로드 및 URL 설정
			String bannerImageUrl = eventService.uploadImage(eventBannerImage);
			String detailImageUrl = eventService.uploadImage(eventDetailImage);

			if (bannerImageUrl != null && detailImageUrl != null) {
				dto.setEventBannerImage(bannerImageUrl);
				dto.setEventDetailImage(detailImageUrl);
				dto.setEventStartDate(startTimestamp);
				dto.setEventEndDate(endTimestamp);

				System.out.println("startTimestamp : " + startTimestamp + " endTimestamp : " + endTimestamp
						+ " eventStartDate : " + eventStartDate);

				boolean saveSuccess = eventService.eventUpdate(dto);

				if (saveSuccess) {
					return ResponseEntity.ok("이벤트생성 완료");
				} else {
					return ResponseEntity.badRequest().body("이벤트생성 실패");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("예외 에러 : " + e.getMessage());
		}
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("알 수 없는 오류가 발생했습니다. 관리자에게 연락하세요.");
	}
}