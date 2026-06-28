package com.miranda.gymlibrary.core.di

import com.miranda.gymlibrary.data.remote.datasource.ExerciseRemoteDataSource
import com.miranda.gymlibrary.data.repository.ExerciseRepositoryImpl
import com.miranda.gymlibrary.domain.repository.ExerciseRepository
import org.koin.dsl.module

val dataModule = module {
    single { ExerciseRemoteDataSource(get()) }
    single<ExerciseRepository> { ExerciseRepositoryImpl(get()) }
}
