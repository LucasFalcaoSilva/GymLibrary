package com.miranda.gymlibrary.core.di

import com.miranda.gymlibrary.presentation.exercisedetail.ExerciseDetailViewModel
import com.miranda.gymlibrary.presentation.exerciselist.ExerciseListViewModel
import com.miranda.gymlibrary.presentation.home.HomeViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel { HomeViewModel(get()) }
    viewModel { params -> ExerciseListViewModel(get(), params.get()) }
    viewModel { params -> ExerciseDetailViewModel(get(), params.get()) }
}
