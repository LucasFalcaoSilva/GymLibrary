package com.miranda.gymlibrary.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.miranda.gymlibrary.core.util.UiState
import com.miranda.gymlibrary.core.util.toUserMessage
import com.miranda.gymlibrary.domain.usecase.GetBodyPartsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class HomeUiState(
    val uiState: UiState<List<String>> = UiState.Loading
)

class HomeViewModel(
    private val getBodyPartsUseCase: GetBodyPartsUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(HomeUiState())
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        loadBodyParts()
    }

    fun loadBodyParts() {
        viewModelScope.launch {
            _state.value = HomeUiState(uiState = UiState.Loading)
            getBodyPartsUseCase()
                .onSuccess { bodyParts ->
                    _state.value = HomeUiState(uiState = UiState.Success(bodyParts))
                }
                .onFailure { e ->
                    _state.value = HomeUiState(uiState = UiState.Error(e.toUserMessage()))
                }
        }
    }
}
