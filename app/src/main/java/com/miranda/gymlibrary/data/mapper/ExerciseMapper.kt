package com.miranda.gymlibrary.data.mapper

import com.miranda.gymlibrary.data.remote.dto.ExerciseDto
import com.miranda.gymlibrary.domain.model.Exercise

fun ExerciseDto.toDomain(): Exercise = Exercise(
    id = id,
    name = name,
    bodyPart = bodyPart,
    equipment = equipment,
    target = target,
    secondaryMuscles = secondaryMuscles,
    instructions = instructions,
    description = description,
    difficulty = difficulty,
    category = category
)
