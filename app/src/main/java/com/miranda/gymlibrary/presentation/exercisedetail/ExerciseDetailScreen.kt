package com.miranda.gymlibrary.presentation.exercisedetail

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.miranda.gymlibrary.core.network.AuthInterceptor
import com.miranda.gymlibrary.core.ui.components.ErrorState
import com.miranda.gymlibrary.core.util.UiState
import com.miranda.gymlibrary.domain.model.Exercise
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExerciseDetailScreen(
    exerciseId: String,
    onBack: () -> Unit,
    viewModel: ExerciseDetailViewModel = koinViewModel(parameters = { parametersOf(exerciseId) })
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val title = when (val s = state.uiState) {
                        is UiState.Success -> s.data.name.replaceFirstChar { it.uppercase() }
                        else -> ""
                    }
                    Text(title)
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (val uiState = state.uiState) {
                is UiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
                is UiState.Error -> ErrorState(
                    message = uiState.message,
                    onRetry = viewModel::loadExercise,
                    modifier = Modifier.align(Alignment.Center)
                )
                is UiState.Success -> ExerciseDetailContent(exercise = uiState.data)
            }
        }
    }
}

@Composable
private fun ExerciseDetailContent(
    exercise: Exercise,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ExerciseGif(exerciseId = exercise.id)

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = exercise.name.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.headlineSmall
            )
            MuscleChipRow(exercise = exercise)
            DescriptionSection(description = exercise.description)
            HorizontalDivider()
            MuscleSection(
                target = exercise.target,
                secondaryMuscles = exercise.secondaryMuscles
            )
            HorizontalDivider()
            InstructionList(instructions = exercise.instructions)
        }
    }
}

@Composable
private fun ExerciseGif(exerciseId: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(AuthInterceptor.gifUrl(exerciseId))
            .crossfade(true)
            .build(),
        contentDescription = null,
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f),
        placeholder = ColorPainter(MaterialTheme.colorScheme.surface),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun MuscleChipRow(exercise: Exercise, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        AssistChip(
            onClick = {},
            label = { Text(exercise.bodyPart.replaceFirstChar { it.uppercase() }) }
        )
        AssistChip(
            onClick = {},
            label = { Text(exercise.equipment.replaceFirstChar { it.uppercase() }) }
        )
        AssistChip(
            onClick = {},
            label = { Text(exercise.difficulty.replaceFirstChar { it.uppercase() }) }
        )
        AssistChip(
            onClick = {},
            label = { Text(exercise.category.replaceFirstChar { it.uppercase() }) }
        )
    }
}

@Composable
private fun DescriptionSection(description: String, modifier: Modifier = Modifier) {
    Text(
        text = description,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
    )
}

@Composable
private fun MuscleSection(
    target: String,
    secondaryMuscles: List<String>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = "Músculos Ativados",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Primário: ${target.replaceFirstChar { it.uppercase() }}",
            style = MaterialTheme.typography.bodyMedium
        )
        Text(
            text = "Secundários: ${secondaryMuscles.joinToString()}",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun InstructionList(instructions: List<String>, modifier: Modifier = Modifier) {
    Column(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Instruções",
            style = MaterialTheme.typography.titleMedium
        )
        instructions.forEachIndexed { index, step ->
            Text(
                text = "${index + 1}. $step",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
