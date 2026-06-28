package com.miranda.gymlibrary.presentation.exerciselist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miranda.gymlibrary.core.util.toUserMessage
import com.miranda.gymlibrary.domain.model.Exercise
import com.miranda.gymlibrary.domain.usecase.GetExercisesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ExerciseListUiState(
    val exercises: List<Exercise> = emptyList(),
    val isLoading: Boolean = false,
    val isLoadingMore: Boolean = false,
    val error: String? = null,
    val hasReachedEnd: Boolean = false
)

class ExerciseListViewModel(
    private val getExercisesUseCase: GetExercisesUseCase,
    private val bodyPart: String
) : ViewModel() {

    private val _state = MutableStateFlow(ExerciseListUiState(isLoading = true))
    val state: StateFlow<ExerciseListUiState> = _state.asStateFlow()

    private var currentOffset = 0
    private val pageSize = 20

    init {
        loadExercises()
    }

    fun loadExercises() {
        viewModelScope.launch {
            currentOffset = 0
            _state.value = ExerciseListUiState(isLoading = true)
            getExercisesUseCase(bodyPart, pageSize, currentOffset)
                .onSuccess { exercises ->
                    currentOffset = exercises.size
                    _state.value = ExerciseListUiState(
                        exercises = exercises,
                        hasReachedEnd = exercises.size < pageSize
                    )
                }
                .onFailure { e ->
                    _state.value = ExerciseListUiState(error = e.toUserMessage())
                }
        }
    }

    fun loadMore() {
        val current = _state.value
        if (current.isLoadingMore || current.hasReachedEnd || current.isLoading) return

        viewModelScope.launch {
            _state.update { it.copy(isLoadingMore = true, error = null) }
            getExercisesUseCase(bodyPart, pageSize, currentOffset)
                .onSuccess { newExercises ->
                    currentOffset += newExercises.size
                    _state.update { state ->
                        state.copy(
                            exercises = state.exercises + newExercises,
                            isLoadingMore = false,
                            hasReachedEnd = newExercises.size < pageSize
                        )
                    }
                }
                .onFailure {
                    _state.update {
                        it.copy(
                            isLoadingMore = false,
                            error = "Erro ao carregar mais exercícios. Tente novamente."
                        )
                    }
                }
        }
    }

    fun dismissError() {
        _state.update { it.copy(error = null) }
    }
}
