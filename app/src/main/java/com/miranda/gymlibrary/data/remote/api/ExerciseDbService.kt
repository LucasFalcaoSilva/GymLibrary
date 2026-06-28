package com.miranda.gymlibrary.data.remote.api

import retrofit2.http.GET

interface ExerciseDbService {
    @GET("exercises/bodyPartList")
    suspend fun getBodyPartList(): List<String>
}
