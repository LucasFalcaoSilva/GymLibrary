package com.miranda.gymlibrary.core.network

import com.miranda.gymlibrary.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader("X-RapidAPI-Key", BuildConfig.RAPIDAPI_KEY)
            .addHeader("X-RapidAPI-Host", HOST)
            .build()
        return chain.proceed(request)
    }

    companion object {
        const val BASE_URL = "https://exercisedb.p.rapidapi.com/"
        const val HOST = "exercisedb.p.rapidapi.com"

        fun gifUrl(exerciseId: String) =
            "${BASE_URL}image?exerciseId=$exerciseId&resolution=360&rapidapi-key=${BuildConfig.RAPIDAPI_KEY}"
    }
}
