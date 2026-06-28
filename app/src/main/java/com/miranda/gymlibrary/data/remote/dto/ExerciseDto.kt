package com.miranda.gymlibrary.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class ExerciseDto(
    val id: String,
    val name: String,
    val bodyPart: String,
    val equipment: String,
    val target: String,
    val secondaryMuscles: List<String>,
    val instructions: List<String>,
    val description: String,
    val difficulty: String,
    val category: String
)
