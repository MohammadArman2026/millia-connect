package com.reyaz.feature.result.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.result.domain.repository.ResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "RESULT_VIEW_MODEL"

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

            ResultEvent.LoadDegree -> getCourseTypes()
            is ResultEvent.LoadResult -> {}
            is ResultEvent.UpdateType -> {
                updateSelectedType(event.type)
                //ResultEvent.LoadCourse(event.type)
            }
            is ResultEvent.UpdateCourse -> {
                updateState { it.copy(selectedCourse = event.course) }
            }

            else -> {}
        }
    }

    private fun getCourseTypes() {
        viewModelScope.launch {
            val typeList = resultRepository.getCourseTypes()
            updateState { it.copy(typeLoading = true) }
            if (typeList.isSuccess) {
                updateState {
                    it.copy(
                        courseTypeList = typeList.getOrDefault(emptyList()),
                        typeLoading = false
                    )
                }
                Log.d(TAG, "Course Types: ${typeList.getOrDefault(emptyList()).size}")
            } else {
                updateState {
                    it.copy(
                        courseTypeList = typeList.getOrDefault(emptyList()),
                        error = typeList.exceptionOrNull()?.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    private fun updateSelectedType(type: String) {
        updateState { it.copy(selectedType = type) }
    }
    private fun setLoading(isLoading: Boolean) {
        updateState { it.copy(isLoading = isLoading) }
    }

    fun setError(error: String?) {
        updateState { it.copy(error = error) }
    }

    private fun updateState(update: (ResultUiState) -> ResultUiState) {
        _uiState.value = update(_uiState.value)
    }
}