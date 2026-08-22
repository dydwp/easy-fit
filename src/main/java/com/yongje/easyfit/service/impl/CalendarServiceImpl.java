package com.yongje.easyfit.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.yongje.easyfit.dto.CalendarMonthResponse;
import com.yongje.easyfit.entity.CalendarRecord;
import com.yongje.easyfit.entity.User;
import com.yongje.easyfit.repository.CalendarRecordRepository;
import com.yongje.easyfit.service.CalendarService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CalendarServiceImpl implements CalendarService {
	
	private final CalendarRecordRepository calendarRecordRepository;
	
	@Override
	public CalendarRecord getRecord(User user, LocalDate date) {
		return calendarRecordRepository.findByUserAndRecordDate(user, date)
				.orElseGet(() -> createEmptyRecord(user, date));
	}
	
	@Override
	public CalendarRecord toggleStamp(User user, LocalDate date) {
		CalendarRecord record = calendarRecordRepository.findByUserAndRecordDate(user, date)
				.orElseGet(() -> createEmptyRecord(user, date));
		
		record.setStamped(!record.isStamped());
		return calendarRecordRepository.save(record);
	}
	
	@Override
	public CalendarRecord saveMemo(User user, LocalDate date, String memo, List<String> bodyParts) {
		CalendarRecord record = calendarRecordRepository.findByUserAndRecordDate(user, date)
				.orElseGet(() -> createEmptyRecord(user, date));
		
		record.setMemo(memo);
		
		if (bodyParts == null || bodyParts.isEmpty()) {
			record.setBodyParts(null);
		} else {
			record.setBodyParts(String.join(",", bodyParts));
		}
		
		return calendarRecordRepository.save(record);
	}
	
	@Override
	public CalendarMonthResponse getMonth(User user, int year, int month) {
		YearMonth yearMonth = YearMonth.of(year, month);
		LocalDate start = yearMonth.atDay(1);
		LocalDate end = yearMonth.atEndOfMonth();
		
		List<CalendarRecord> records = calendarRecordRepository.findByUserAndRecordDateBetween(user, start, end);
		
		int maxStreak = calculateMaxStreak(records, start, end);
		
		return new CalendarMonthResponse(records, maxStreak);
	}
	
	private CalendarRecord createEmptyRecord(User user, LocalDate date) {
		CalendarRecord record = new CalendarRecord();
		record.setUser(user);
		record.setRecordDate(date);
		record.setStamped(false);
		record.setMemo("");
		return record;
	}
	
	private int calculateMaxStreak(List<CalendarRecord> records, LocalDate start, LocalDate end) {
		Map<LocalDate, Boolean> stampedMap = new HashMap<>();
		for (CalendarRecord r : records) {
			stampedMap.put(r.getRecordDate(), r.isStamped());
		}
		
		int max = 0;
		int current = 0;
		for (LocalDate d = start; !d.isAfter(end); d = d.plusDays(1)) {
			if (Boolean.TRUE.equals(stampedMap.get(d))) {
				current++;
				max = Math.max(max, current);
			} else {
				current = 0;
			}
		}
		return max;
	}
}