package com.miranda.gymlibrary.core.di

import com.miranda.gymlibrary.domain.usecase.GetBodyPartsUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetBodyPartsUseCase(get()) }
}
