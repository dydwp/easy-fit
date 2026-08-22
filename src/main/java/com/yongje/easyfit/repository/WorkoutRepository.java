package com.yongje.easyfit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.yongje.easyfit.entity.Workout;

public interface WorkoutRepository extends JpaRepository<Workout, Long> {
	
	List<Workout> findByBodyPart(String bodyPart);
}
