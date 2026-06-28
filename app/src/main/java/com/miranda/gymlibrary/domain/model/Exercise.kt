package com.miranda.gymlibrary.domain.model

data class Exercise(
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
