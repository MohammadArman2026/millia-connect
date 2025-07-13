package com.reyaz.feature.notice.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.core.common.utils.NetworkManager
import com.reyaz.core.common.utils.Resource
import com.reyaz.core.network.model.DownloadResult
import com.reyaz.feature.notice.data.NoticeRepository
import com.reyaz.feature.notice.data.model.NoticeType
import com.reyaz.feature.notice.domain.model.TabConfig
import com.reyaz.feature.notice.domain.usecase.GetNoticeFromNetworkUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

private const val TAG = "NOTICE_VIEW_MODEL"
//private const val link = "https://tourism.gov.in/sites/default/files/2019-04/dummy-pdf_2.pdf"
//private const val link = "http://jmicoe.in/pdf25/online%20offline%20extension%20notice.jpeg.pdf"

class NoticeViewModel(
    private val noticeRepository: NoticeRepository,
    private val getNoticeFromNetworkUseCase: GetNoticeFromNetworkUseCase,
    private val networkManager: NetworkManager
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState = _uiState.asStateFlow()

    private var observeNoticeJob: Job? = null
    private var refreshJob: Job? = null
    private var markAsReadJob: Job? = null

    init {
        viewModelScope.launch {

            networkManager.observeInternetConnectivity().collect { isInternetAvailable ->
                if (isInternetAvailable) {
//                    Log.d(TAG, "Yes internet")
                    onTabSelect(tab = TabConfig.entries[uiState.value.selectedTabIndex])
                } else {
//                    Log.d(TAG, "No internet")
                    _uiState.update { currentNoticeUiState ->
                        currentNoticeUiState.copy(errorMessage = "No internet connection")
                    }
                }
            }
        }
    }

    fun event(event: NoticeEvent) {
        when (event) {
            /*is NoticeEvent.ObserveNotice -> {
                observeLocalNotices(event.type)
                refreshRemoteNotice(event.type, forceRefresh = false)
            }*/

            is NoticeEvent.FetchLocalNotice -> observeLocalNotices(event.type)
            is NoticeEvent.FetchRemoteNotice -> refreshRemoteNotice(event.type, event.forceRefresh)
            is NoticeEvent.DownloadPdf -> downloadPdf(url = event.url, title = event.title)
            is NoticeEvent.DeleteFileByPath -> deletePdfByPath(
                title = event.title,
                path = event.path
            )

            is NoticeEvent.OnTabClick -> onTabSelect(tab = event.tab)
            /*else -> {
                Log.d(TAG, "Unknown event: $event")
            }*/
        }
    }

    private fun onTabSelect(tab: TabConfig) {
        updateState { it.copy(selectedTabIndex = tab.ordinal, errorMessage = null) }
        observeLocalNotices(type = tab.type)
//        refreshRemoteNotice(type = tab.type, forceRefresh = false)
        markAsRead(tab.type.typeId)
    }

    private fun observeLocalNotices(type: NoticeType) {
        observeNoticeJob?.cancel()
        observeNoticeJob = viewModelScope.launch {
            var autoRefreshJob: Job? = null

            noticeRepository.observeNotice(type).collect { notices ->
                //Log.d(TAG, "Notice type: ${type.typeId}")
                updateState { it.copy(noticeList = notices) }

                // Only auto-refresh if no manual refresh is running
                if (refreshJob?.isActive != true) {
                    autoRefreshJob?.cancel()
                    autoRefreshJob = launch {
                        refreshRemoteNoticeInternal(
                            type = type,
                            forceRefresh = notices.isEmpty()
                        )
                    }
                }
            }
        }
    }

    // Public method for manual refresh (called from UI)
    private fun refreshRemoteNotice(type: NoticeType, forceRefresh: Boolean) {
        // Cancel previous refresh and start new one
        refreshJob?.cancel()
        refreshJob = viewModelScope.launch {
            refreshRemoteNoticeInternal(type = type, forceRefresh = forceRefresh)
        }
    }

    // Internal method that handles the actual refresh logic
    private suspend fun refreshRemoteNoticeInternal(type: NoticeType, forceRefresh: Boolean) {
        getNoticeFromNetworkUseCase(
            type = type,
            forceRefresh = forceRefresh
        ).collect { resource ->
            when (resource) {
                is Resource.Error -> setResetError(error = resource.message)
                is Resource.Loading -> updateState {
                    it.copy(
                        isLoading = true,
                        errorMessage = null
                    )
                }

                is Resource.Success -> updateState {
                    it.copy(
                        isLoading = false,
                        errorMessage = null
                    )
                }
            }
        }
    }

    private fun setResetError(error: String? = null) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = false, errorMessage = error) }
            delay(2000)
            updateState { it.copy(errorMessage = null) }
        }
    }

    private fun markAsRead(typeId: String) {
        markAsReadJob?.cancel()
        markAsReadJob = viewModelScope.launch(Dispatchers.IO) {
            delay(5000)
            noticeRepository.markNoticesAsRead(typeId)
        }
    }

    private fun downloadPdf(url: String, title: String) {
        Log.d(TAG, "Download url: $url")
        viewModelScope.launch {
            noticeRepository.downloadPdf(url = url, fileName = title)
                .collect { downloadResult ->
                    when (downloadResult) {
                        is DownloadResult.Error -> {
                            updateState { it.copy(errorMessage = downloadResult.exception.message) }
                        }

                        else -> {
                            // Handle other download states if needed
                        }
                    }
                }
        }
    }

    private fun deletePdfByPath(path: String, title: String) {
        viewModelScope.launch {
            noticeRepository.deleteFileByPath(path, title)
        }
    }

    private fun updateState(update: (NoticeUiState) -> NoticeUiState) {
        _uiState.update { update(it) }
    }

    override fun onCleared() {
        super.onCleared()
        observeNoticeJob?.cancel()
        refreshJob?.cancel()
        markAsReadJob?.cancel()
    }
}