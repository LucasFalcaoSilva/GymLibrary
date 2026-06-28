package com.miranda.gymlibrary.domain.usecase

import com.miranda.gymlibrary.domain.repository.ExerciseRepository

class GetBodyPartsUseCase(private val repository: ExerciseRepository) {
    suspend operator fun invoke(): Result<List<String>> = repository.getBodyParts()
}
