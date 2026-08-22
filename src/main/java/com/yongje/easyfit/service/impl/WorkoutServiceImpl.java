package com.yongje.easyfit.service.impl;

import com.yongje.easyfit.entity.Workout;
import com.yongje.easyfit.repository.WorkoutRepository;
import com.yongje.easyfit.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutRepository workoutRepository;

    @Override
    public List<Workout> getWorkoutsByPart(String bodyPart) {
        return workoutRepository.findByBodyPart(bodyPart);
    }

    @Override
    public List<Workout> getAllWorkouts() {
        return workoutRepository.findAll();
    }

}