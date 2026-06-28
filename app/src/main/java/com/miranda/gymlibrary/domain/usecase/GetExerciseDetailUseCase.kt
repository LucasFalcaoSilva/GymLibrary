package com.miranda.gymlibrary.domain.usecase

import com.miranda.gymlibrary.domain.model.Exercise
import com.miranda.gymlibrary.domain.repository.ExerciseRepository

class GetExerciseDetailUseCase(private val repository: ExerciseRepository) {
    suspend operator fun invoke(exerciseId: String): Result<Exercise> =
        repository.getExerciseById(exerciseId)
}
