package com.miranda.gymlibrary.presentation.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.DirectionsRun
import androidx.compose.material.icons.automirrored.filled.DirectionsWalk
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.PanTool
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RadioButtonUnchecked
import androidx.compose.material.icons.filled.SportsMma
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.miranda.gymlibrary.core.ui.components.ErrorState
import com.miranda.gymlibrary.core.util.UiState
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onBodyPartSelected: (String) -> Unit,
    viewModel: HomeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    Scaffold(
        topBar = { TopAppBar(title = { Text("GymLibrary") }) }
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
                    onRetry = viewModel::loadBodyParts,
                    modifier = Modifier.align(Alignment.Center)
                )
                is UiState.Success -> {
                    if (uiState.data.isEmpty()) {
                        Text(
                            text = "Nenhum grupo encontrado",
                            modifier = Modifier.align(Alignment.Center)
                        )
                    } else {
                        BodyPartGrid(
                            bodyParts = uiState.data,
                            onBodyPartSelected = onBodyPartSelected
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun BodyPartGrid(
    bodyParts: List<String>,
    onBodyPartSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier.fillMaxSize()
    ) {
        items(bodyParts) { bodyPart ->
            BodyPartCard(
                bodyPart = bodyPart,
                onClick = { onBodyPartSelected(bodyPart) }
            )
        }
    }
}

@Composable
private fun BodyPartCard(
    bodyPart: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = iconForBodyPart(bodyPart),
                contentDescription = null,
                modifier = Modifier.size(40.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = bodyPart.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}

private fun iconForBodyPart(bodyPart: String): ImageVector = when (bodyPart) {
    "back" -> Icons.Default.FitnessCenter
    "chest" -> Icons.Default.FavoriteBorder
    "upper arms" -> Icons.Default.SportsMma
    "lower arms" -> Icons.Default.PanTool
    "shoulders" -> Icons.Default.AccessibilityNew
    "upper legs" -> Icons.AutoMirrored.Filled.DirectionsWalk
    "lower legs" -> Icons.AutoMirrored.Filled.DirectionsRun
    "waist" -> Icons.Default.RadioButtonUnchecked
    "cardio" -> Icons.Default.MonitorHeart
    "neck" -> Icons.Default.Person
    else -> Icons.Default.FitnessCenter
}
