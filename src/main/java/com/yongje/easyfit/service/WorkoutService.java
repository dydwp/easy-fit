package com.yongje.easyfit.service;

import java.util.List;

import com.yongje.easyfit.entity.Workout;

public interface WorkoutService {
	
	List<Workout> getWorkoutsByPart(String bodyPart);

    List<Workout> getAllWorkouts();
}
