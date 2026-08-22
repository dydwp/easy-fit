package com.yongje.easyfit.dto;

import java.util.List;

import com.yongje.easyfit.entity.CalendarRecord;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
@AllArgsConstructor
public class CalendarMonthResponse {
	private List<CalendarRecord> records;
	private int maxStreak;
}
