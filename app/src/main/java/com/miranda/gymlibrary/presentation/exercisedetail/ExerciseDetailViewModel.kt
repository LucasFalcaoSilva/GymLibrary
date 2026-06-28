package com.miranda.gymlibrary.presentation.exercisedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miranda.gymlibrary.core.util.UiState
import com.miranda.gymlibrary.core.util.toUserMessage
import com.miranda.gymlibrary.domain.model.Exercise
import com.miranda.gymlibrary.domain.usecase.GetExerciseDetailUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class ExerciseDetailUiState(
    val uiState: UiState<Exercise> = UiState.Loading
)

class ExerciseDetailViewModel(
    private val getExerciseDetailUseCase: GetExerciseDetailUseCase,
    private val exerciseId: String
) : ViewModel() {

    private val _state = MutableStateFlow(ExerciseDetailUiState())
    val state: StateFlow<ExerciseDetailUiState> = _state.asStateFlow()

    init {
        loadExercise()
    }

    fun loadExercise() {
        viewModelScope.launch {
            _state.value = ExerciseDetailUiState(uiState = UiState.Loading)
            getExerciseDetailUseCase(exerciseId)
                .onSuccess { exercise ->
                    _state.value = ExerciseDetailUiState(uiState = UiState.Success(exercise))
                }
                .onFailure { e ->
                    _state.value = ExerciseDetailUiState(uiState = UiState.Error(e.toUserMessage()))
                }
        }
    }
}
