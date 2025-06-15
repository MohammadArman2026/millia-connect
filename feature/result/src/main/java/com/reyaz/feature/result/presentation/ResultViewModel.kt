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
    private val _uiState = MutableStateFlow(ResultUiState(
        courseLoading = false,
    ))
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    init {
        onEvent(ResultEvent.LoadDegree)
    }

    fun onEvent(event: ResultEvent) {
        when (event) {
            ResultEvent.LoadDegree -> getCourseTypes()
            is ResultEvent.LoadCourse -> {
                getCourses()
            }
            is ResultEvent.LoadResult -> {}
            is ResultEvent.UpdateType -> {
                updateSelectedType(event.typeIndex)
                //ResultEvent.LoadCourse(event.type)
            }
            is ResultEvent.UpdateCourse -> {
                updateState { it.copy(selectedCourseIndex = event.selectedIndex) }
            }

            else -> {}
        }
    }

    private fun getCourseTypes() {
        viewModelScope.launch {
            val typeList = resultRepository.getCourseTypes()
            // Log.d(TAG, "List: $typeList")
            updateState { it.copy(typeLoading = true) }
            if (typeList.isSuccess) {
                updateState {
                    it.copy(
                        courseTypeList = typeList.getOrDefault(emptyList()),
                        typeLoading = false
                    )
                }
                 Log.d(TAG, "Course Types: ${typeList.getOrDefault(emptyList())}")
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

    private fun getCourses() {
        viewModelScope.launch {
            // Log.d(TAG, "Loading Course")
            updateState { it.copy(courseLoading = true, courseNameList = emptyList()) }
            val typeList = resultRepository.getCourses(uiState.value.courseTypeList[uiState.value.selectedTypeIndex!!].id)
            if (typeList.isSuccess) {
                updateState {
                    it.copy(
                        courseNameList = typeList.getOrDefault(emptyList()),
                        courseLoading = false
                    )
                }
                // Log.d`(TAG, "Course Types: ${typeList.getOrDefault(emptyList()).size}")
            } else {
                updateState {
                    it.copy(
                        courseNameList = typeList.getOrDefault(emptyList()),
                        error = typeList.exceptionOrNull()?.message,
                        courseLoading = false
                    )
                }
            }
        }
    }

    private fun updateSelectedType(type: Int) {
        updateState { it.copy(selectedTypeIndex = type, selectedCourseIndex = null) }
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