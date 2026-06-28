package com.miranda.gymlibrary.presentation.navigation

import android.net.Uri
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.miranda.gymlibrary.presentation.exercisedetail.ExerciseDetailScreen
import com.miranda.gymlibrary.presentation.exerciselist.ExerciseListScreen
import com.miranda.gymlibrary.presentation.home.HomeScreen

@Composable
fun NavGraph() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = HomeRoute.route
    ) {
        composable(HomeRoute.route) {
            HomeScreen(
                onBodyPartSelected = { bodyPart ->
                    navController.navigate(ExerciseListRoute.createRoute(bodyPart))
                }
            )
        }

        composable(
            route = ExerciseListRoute.route,
            arguments = listOf(navArgument("bodyPart") { type = NavType.StringType })
        ) { backStackEntry ->
            val bodyPart = Uri.decode(
                backStackEntry.arguments?.getString("bodyPart").orEmpty()
            )
            ExerciseListScreen(
                bodyPart = bodyPart,
                onExerciseSelected = { exerciseId ->
                    navController.navigate(ExerciseDetailRoute.createRoute(exerciseId))
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            route = ExerciseDetailRoute.route,
            arguments = listOf(navArgument("exerciseId") { type = NavType.StringType })
        ) { backStackEntry ->
            val exerciseId = Uri.decode(
                backStackEntry.arguments?.getString("exerciseId").orEmpty()
            )
            ExerciseDetailScreen(
                exerciseId = exerciseId,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
