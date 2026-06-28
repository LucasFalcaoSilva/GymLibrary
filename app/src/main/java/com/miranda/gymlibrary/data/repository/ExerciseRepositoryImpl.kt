package com.miranda.gymlibrary.data.repository

import com.miranda.gymlibrary.data.mapper.toDomain
import com.miranda.gymlibrary.data.remote.datasource.ExerciseRemoteDataSource
import com.miranda.gymlibrary.domain.model.Exercise
import com.miranda.gymlibrary.domain.repository.ExerciseRepository

class ExerciseRepositoryImpl(
    private val remoteDataSource: ExerciseRemoteDataSource
) : ExerciseRepository {
    override suspend fun getBodyParts(): Result<List<String>> =
        remoteDataSource.getBodyParts()

    override suspend fun getExercisesByBodyPart(
        bodyPart: String,
        limit: Int,
        offset: Int
    ): Result<List<Exercise>> =
        remoteDataSource.getExercisesByBodyPart(bodyPart, limit, offset)
            .map { dtoList -> dtoList.map { it.toDomain() } }

    override suspend fun getExerciseById(id: String): Result<Exercise> =
        remoteDataSource.getExerciseById(id)
}
