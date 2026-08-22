package com.yongje.easyfit.service;

import java.time.LocalDate;
import java.util.List;

import com.yongje.easyfit.dto.CalendarMonthResponse;
import com.yongje.easyfit.entity.CalendarRecord;
import com.yongje.easyfit.entity.User;

public interface CalendarService {
	
	CalendarRecord getRecord(User user, LocalDate date);
	
	CalendarRecord toggleStamp(User user, LocalDate date);
	
	CalendarRecord saveMemo(User user, LocalDate date, String memo, List<String> bodyParts);
	
	CalendarMonthResponse getMonth(User user, int year, int month);
}