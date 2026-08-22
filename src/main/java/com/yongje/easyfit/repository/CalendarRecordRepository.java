package com.yongje.easyfit.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yongje.easyfit.entity.CalendarRecord;
import com.yongje.easyfit.entity.User;

public interface CalendarRecordRepository extends JpaRepository<CalendarRecord, Long>{
	
	Optional<CalendarRecord> findByUserAndRecordDate(User user, LocalDate recordDate);
	
	List<CalendarRecord> findByUserAndRecordDateBetween(User user, LocalDate start, LocalDate end);
}
