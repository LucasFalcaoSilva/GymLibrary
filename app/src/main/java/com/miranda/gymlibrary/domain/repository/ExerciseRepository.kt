package com.miranda.gymlibrary.domain.repository

import com.miranda.gymlibrary.domain.model.Exercise

interface ExerciseRepository {
    suspend fun getBodyParts(): Result<List<String>>
    suspend fun getExercisesByBodyPart(bodyPart: String, limit: Int, offset: Int): Result<List<Exercise>>
}
