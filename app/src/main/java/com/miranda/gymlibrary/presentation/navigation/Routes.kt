package com.miranda.gymlibrary.presentation.navigation

import android.net.Uri

object HomeRoute {
    const val route = "home"
}

object ExerciseListRoute {
    const val route = "exercise-list/{bodyPart}"
    fun createRoute(bodyPart: String) = "exercise-list/${Uri.encode(bodyPart)}"
}

object ExerciseDetailRoute {
    const val route = "exercise-detail/{exerciseId}"
    fun createRoute(exerciseId: String) = "exercise-detail/${Uri.encode(exerciseId)}"
}
