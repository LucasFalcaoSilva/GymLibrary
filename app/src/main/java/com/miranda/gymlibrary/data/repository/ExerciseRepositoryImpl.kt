package com.miranda.gymlibrary.data.repository

import com.miranda.gymlibrary.data.remote.datasource.ExerciseRemoteDataSource
import com.miranda.gymlibrary.domain.repository.ExerciseRepository

class ExerciseRepositoryImpl(
    private val remoteDataSource: ExerciseRemoteDataSource
) : ExerciseRepository {
    override suspend fun getBodyParts(): Result<List<String>> =
        remoteDataSource.getBodyParts()
}
