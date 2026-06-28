package com.miranda.gymlibrary.core.di

import com.miranda.gymlibrary.domain.usecase.GetBodyPartsUseCase
import com.miranda.gymlibrary.domain.usecase.GetExerciseDetailUseCase
import com.miranda.gymlibrary.domain.usecase.GetExercisesUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetBodyPartsUseCase(get()) }
    factory { GetExercisesUseCase(get()) }
    factory { GetExerciseDetailUseCase(get()) }
}
