package com.reyaz.feature.result.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.feature.result.domain.repository.ResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "RESULT_VIEW_MODEL"

class ResultViewModel(
    private val resultRepository: ResultRepository,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(ResultUiState())
    val uiState: StateFlow<ResultUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            onEvent(ResultEvent.LoadSavedResults)
            networkManager.observeInternetConnectivity().collect { isNetworkAvailable ->
                if (isNetworkAvailable){
                    onEvent(ResultEvent.Initialize)
                } else {
                    updateState { it.copy(error = "No Internet Connection") }
                }
            }
        }
    }

    fun onEvent(event: ResultEvent) {
        when (event) {
            ResultEvent.Initialize -> initializeRemoteComponents()
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

            is ResultEvent.RefreshResults -> refreshResultsFromRemote()
//            is ResultEvent.DeleteFileByPath -> deletePdfByPath(event.path, event.listId)

            is ResultEvent.MarkAsRead -> markAsRead(event.courseId)
            else -> {}
        }
    }

    private fun initializeRemoteComponents() {
        onEvent(ResultEvent.LoadDegree)
        //onEvent(ResultEvent.RefreshResults)   // todo: remove this
    }

    private fun markAsRead(courseId: String) {
        viewModelScope.launch {
            resultRepository.markCourseAsRead(courseId)
        }
    }

    private fun refreshResultsFromRemote() {
        viewModelScope.launch { resultRepository.refreshLocalResults(shouldNotify = false) }
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
                Log.d(TAG, "Results in viewmodel: ${resultHistories}")
                updateState { it.copy(isLoading = false, historyList = resultHistories) }
            }
        }
    }

    private fun getCourseTypes() {
        viewModelScope.launch {
            updateState { it.copy(typeLoading = true, error = null) }
            val typeList = resultRepository.getCourseTypes()
            // Log.d(TAG, "List: $typeList")
            if (typeList.isSuccess) {
                 Log.d(TAG, "type loading SUCCESS & List: $typeList")
                updateState {
                    it.copy(
                        courseTypeList = typeList.getOrDefault(emptyList()),
                        typeLoading = false
                    )
                }
                // Log.d(TAG, "Course Types: ${typeList.getOrDefault(emptyList())}")
            } else {
                Log.d(TAG, "type loading FAILED")
                updateState {
                    it.copy(
                        courseTypeList = emptyList(),
                        error = typeList.exceptionOrNull()?.message,
                        typeLoading = false
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
                        courseNameList = emptyList(),
                        error = typeList.exceptionOrNull()?.message,
                        courseLoading = false
                    )
                }
            }
        }
    }

    private fun getResult() {
        viewModelScope.launch {
             Log.d(TAG, "Loading Course")
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
        _uiState.update(update)
    }

    private fun downloadPdf(url: String, listId: String, listTitle: String) {
//        Log.d(TAG, "Download url: $url")
        viewModelScope.launch {
            if (networkManager.observeInternetConnectivity().first()) {
                updateState { it.copy(error = null) }
                resultRepository.downloadPdf(url = url, listId = listId, fileName = listTitle)
            }
            else
                updateState { it.copy(error = "No Internet Connection") }
        }
    }

    private fun deletePdfByPath(path: String, listId: String) {
        viewModelScope.launch {
            resultRepository.deleteFileByPath(path, listId = listId)
        }
    }
}