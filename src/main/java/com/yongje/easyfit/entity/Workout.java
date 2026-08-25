package com.yongje.easyfit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "workout")
@Getter @Setter
@NoArgsConstructor
public class Workout {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "body_part", nullable = false, length = 20)
	private String bodyPart;
	
	@Column(nullable = false, length = 100)
	private String title;
	
	@Column(name = "img_url", length = 500)
	private String imgUrl;
	
	@Column(length = 100)
	private String tags;
	
	@Column(columnDefinition = "TEXT")
	private String pose;
	
	@Column(name = "target_muscle", length = 200)
	private String targetMuscle;
	
	@Column(columnDefinition = "TEXT")
	private String caution;
	
	@Column(name = "video_url", length = 500)
	private String videoUrl;
}
