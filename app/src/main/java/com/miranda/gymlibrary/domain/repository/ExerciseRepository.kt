package com.miranda.gymlibrary.domain.repository

interface ExerciseRepository {
    suspend fun getBodyParts(): Result<List<String>>
}
