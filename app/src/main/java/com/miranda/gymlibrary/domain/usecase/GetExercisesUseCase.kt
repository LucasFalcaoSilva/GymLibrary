package com.miranda.gymlibrary.domain.usecase

import com.miranda.gymlibrary.domain.model.Exercise
import com.miranda.gymlibrary.domain.repository.ExerciseRepository

class GetExercisesUseCase(private val repository: ExerciseRepository) {
    suspend operator fun invoke(bodyPart: String, limit: Int, offset: Int): Result<List<Exercise>> =
        repository.getExercisesByBodyPart(bodyPart, limit, offset)
}
