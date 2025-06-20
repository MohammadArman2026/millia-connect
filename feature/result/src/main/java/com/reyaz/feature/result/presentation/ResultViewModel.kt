package com.reyaz.feature.result.presentation

import android.content.Context
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
    private val resultRepository: ResultRepository,
    context: Context
) : ViewModel() {
    private val _uiState = MutableStateFlow(
        ResultUiState(
            courseLoading = false,
        )
    )
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    init {
        onEvent(ResultEvent.LoadSavedResults)
        onEvent(ResultEvent.LoadDegree)
        onEvent(ResultEvent.RefreshResults)
    }

    fun onEvent(event: ResultEvent) {
        when (event) {
            ResultEvent.LoadDegree -> getCourseTypes()
            is ResultEvent.LoadCourse -> {
                getCourses()
            }

            is ResultEvent.LoadResult -> {
                saveCourse()
                getResult()
            }

            is ResultEvent.UpdateType -> {
                updateSelectedType(event.typeIndex)
                //ResultEvent.LoadCourse(event.type)
            }

            is ResultEvent.UpdateCourse -> {
                updateState { it.copy(selectedCourseIndex = event.selectedIndex) }
            }

            ResultEvent.LoadSavedResults -> {
                observeSavedResults()
            }

            is ResultEvent.DeleteCourse -> onDeleteCourse(event.courseId)

            is ResultEvent.ToggleDownload -> {
                Log.d(TAG, "Toggle download invoked with event: $event")
                event.url?.let {
                    downloadPdf(
                        url = it,
                        listId = event.listId,
                        listTitle = event.title
                    )
                } ?: run {
                    event.path?.let { deletePdfByPath(path = it, listId = event.listId) }
                }
            }

            is ResultEvent.DownloadPdf -> downloadPdf(
                url = event.pdfUrl,
                listId = event.listId,
                listTitle = event.title
            )

            is ResultEvent.RefreshResults -> refreshResults()
//            is ResultEvent.DeleteFileByPath -> deletePdfByPath(event.path, event.listId)

            is ResultEvent.MarkAsRead -> markAsRead(event.courseId)
            else -> {}
        }
    }

    private fun markAsRead(courseId: String) {
        viewModelScope.launch {
            resultRepository.markCourseAsRead(courseId)
        }
    }

    private fun refreshResults() {
        viewModelScope.launch { resultRepository.refreshLocalResults() }
    }

    private fun saveCourse() {
        viewModelScope.launch {
            resultRepository.saveCourse(
                courseId = uiState.value.selectedCourseId,
                courseName = uiState.value.selectedCourse,
                courseTypeId = uiState.value.selectedTypeId,
                courseType = uiState.value.selectedType,
                phdDepartmentId = uiState.value.selectedPhdDepartmentId,
                phdDepartment = uiState.value.selectedPhdDepartment
            )
        }
    }

    private fun onDeleteCourse(courseId: String) {
        viewModelScope.launch {
            resultRepository.deleteCourse(courseId)
        }
    }

    private fun observeSavedResults() {
        viewModelScope.launch {
            resultRepository.observeResults().collect { resultHistories ->
//                Log.d(TAG, "Results in viewmodel: ${resultHistories}")
                updateState { it.copy(isLoading = false, historyList = resultHistories) }
            }
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
                // Log.d(TAG, "Course Types: ${typeList.getOrDefault(emptyList())}")
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
            val typeList = resultRepository.getCourses(uiState.value.selectedTypeId)
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

    private fun getResult() {
        viewModelScope.launch {
            // Log.d(TAG, "Loading Course")
            updateState { it.copy(isLoading = true) }
            val result = resultRepository.getResult(
                type = uiState.value.selectedTypeId,
                course = uiState.value.selectedCourseId
            )
            Log.d(TAG, "course id: ${ uiState.value.selectedTypeId}")
            if (result.isSuccess) {
                updateState { it.copy(isLoading = false) }
            } else {
                updateState {
                    it.copy(
                        error = result.exceptionOrNull()?.message,
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

    private fun downloadPdf(url: String, listId: String, listTitle: String) {
        Log.d(TAG, "Download url: $url")
        viewModelScope.launch {
            resultRepository.downloadPdf(url = url, listId = listId, fileName = listTitle)
                .collect { downloadResult ->
                    /* when(downloadResult){
                         is DownloadResult.Error -> {
                             updateState { it.copy(error = downloadResult.exception) }
                         }
                         is DownloadResult.Progress -> {
                             updateState { it.copy(progress = it.progress) }
                         }
                         is DownloadResult.Success -> {
                             updateState { it.copy(progress = null) }
                         }
                     }*/
                }
        }
    }

    private fun deletePdfByPath(path: String, listId: String) {
        viewModelScope.launch {
            resultRepository.deleteFileByPath(path, listId = listId)
        }
    }
}