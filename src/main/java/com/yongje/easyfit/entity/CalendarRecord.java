package com.yongje.easyfit.entity;

import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "calendar_record", uniqueConstraints = {
		@UniqueConstraint(columnNames = {"user_id", "record_date"})
		
})
@Getter @Setter
@NoArgsConstructor
public class CalendarRecord {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;
	
	@Column(name = "record_date", nullable = false)
	private LocalDate recordDate;
	
	@Column(nullable = false)
	private boolean stamped;
	
	@Lob
	private String memo;
	
	@Column(name = "body_parts", length = 100)
	private String bodyParts;
}
