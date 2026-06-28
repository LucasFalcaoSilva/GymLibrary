package com.miranda.gymlibrary.data.remote.datasource

import com.miranda.gymlibrary.data.remote.api.ExerciseDbService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ExerciseRemoteDataSource(private val service: ExerciseDbService) {
    suspend fun getBodyParts(): Result<List<String>> = withContext(Dispatchers.IO) {
        runCatching { service.getBodyPartList() }
    }
}
