package com.miranda.gymlibrary.core.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.miranda.gymlibrary.BuildConfig
import com.miranda.gymlibrary.core.network.AuthInterceptor
import com.miranda.gymlibrary.data.remote.api.ExerciseDbService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

val networkModule = module {
    single {
        check(BuildConfig.RAPIDAPI_KEY.isNotBlank()) {
            "RAPIDAPI_KEY not set — add it to local.properties"
        }
        OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor())
            .apply {
                if (BuildConfig.DEBUG) {
                    addInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BODY
                    })
                }
            }
            .build()
    }

    single {
        val json = Json { ignoreUnknownKeys = true }
        Retrofit.Builder()
            .baseUrl(AuthInterceptor.BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    single { get<Retrofit>().create(ExerciseDbService::class.java) }
}
