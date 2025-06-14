package com.reyaz.feature.result.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.result.domain.repository.ResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResultViewModel(
    private val resultRepository: ResultRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    init {
        onEvent(ResultEvent.LoadDegree)
    }

    fun onEvent(event: ResultEvent) {
        when (event) {
            is ResultEvent.LoadCourse -> {

            }
            ResultEvent.LoadDegree -> getDegreeList()
            is ResultEvent.LoadResult -> {}
            else -> {}
        }
    }

    fun setLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    fun setError(error: String?) {
        updateState { it.copy(error = error) }
    }
    private fun getDegreeList(){
        viewModelScope.launch {
            resultRepository.getDegree()
        }
    }
    private fun updateState(update: (ResultUiState) -> ResultUiState) {
        _uiState.value = update(_uiState.value)
    }
}