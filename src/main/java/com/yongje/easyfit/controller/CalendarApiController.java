package com.yongje.easyfit.controller;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.yongje.easyfit.dto.CalendarMonthResponse;
import com.yongje.easyfit.dto.MemoRequest;
import com.yongje.easyfit.entity.CalendarRecord;
import com.yongje.easyfit.security.PrincipalDetails;
import com.yongje.easyfit.service.CalendarService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/calendar")
@RequiredArgsConstructor
public class CalendarApiController {

	private final CalendarService calendarService;

	@GetMapping("/month")
	public CalendarMonthResponse getMonth(@AuthenticationPrincipal PrincipalDetails principal,
											@RequestParam int year, @RequestParam int month) {
		return calendarService.getMonth(principal.getUser(), year, month);
	}

	@GetMapping("/{date}")
	public CalendarRecord getRecord(@AuthenticationPrincipal PrincipalDetails principal,
									@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return calendarService.getRecord(principal.getUser(), date);
	}

	@PostMapping("/{date}/stamp")
	public CalendarRecord toggleStamp(@AuthenticationPrincipal PrincipalDetails principal,
										@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
		return calendarService.toggleStamp(principal.getUser(), date);
	}

	@PostMapping("/{date}/memo")
	public CalendarRecord saveMemo(@AuthenticationPrincipal PrincipalDetails principal,
									@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
									@RequestBody MemoRequest request) {
		return calendarService.saveMemo(principal.getUser(), date, request.getMemo(), request.getBodyParts());
	}
}