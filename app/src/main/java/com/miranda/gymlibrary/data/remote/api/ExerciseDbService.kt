package com.miranda.gymlibrary.data.remote.api

import com.miranda.gymlibrary.data.remote.dto.ExerciseDto
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ExerciseDbService {
    @GET("exercises/bodyPartList")
    suspend fun getBodyPartList(): List<String>

    @GET("exercises/bodyPart/{bodyPart}")
    suspend fun getExercisesByBodyPart(
        @Path("bodyPart") bodyPart: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): List<ExerciseDto>

    @GET("exercises/exercise/{id}")
    suspend fun getExerciseById(@Path("id") id: String): ExerciseDto
}
