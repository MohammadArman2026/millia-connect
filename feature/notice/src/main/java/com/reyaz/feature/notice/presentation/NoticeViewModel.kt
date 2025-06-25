package com.reyaz.feature.notice.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
import kotlinx.coroutines.launch

private const val TAG = "NOTICE_VIEW_MODEL"
//private const val link = "https://tourism.gov.in/sites/default/files/2019-04/dummy-pdf_2.pdf"
//private const val link = "http://jmicoe.in/pdf25/online%20offline%20extension%20notice.jpeg.pdf"

class NoticeViewModel(
    private val noticeRepository: NoticeRepository,
    private val getNoticeFromNetworkUseCase: GetNoticeFromNetworkUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState = _uiState.asStateFlow()

    private var observeJob: Job? = null

    init {
        onTabSelect(tab = TabConfig.entries[0])
    }

    fun event(event: NoticeEvent) {
        when (event) {
            is NoticeEvent.ObserveNotice -> {
                observeLocalNotices(event.type)
                refreshRemoteNotice(event.type)
            }

            is NoticeEvent.FetchLocalNotice -> observeLocalNotices(event.type)
            is NoticeEvent.RefreshNotice -> refreshRemoteNotice(event.type)
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
        refreshRemoteNotice(type = tab.type)
        observeLocalNotices(type = tab.type)
        markAsRead(tab.type.typeId)
    }

    private fun markAsRead(typeId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            delay(5000)
            noticeRepository.markNoticesAsRead(typeId)
        }
    }

    private fun refreshRemoteNotice(type: NoticeType) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true, errorMessage = null) }
            val refreshResult = getNoticeFromNetworkUseCase(type = type, forceRefresh = true)
            if (refreshResult.isSuccess) {
                updateState { it.copy(isLoading = false) }
            } else {
                setError(error = refreshResult.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    private fun setError(error: String? = null) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = false, errorMessage = error) }
            delay(2000)
            updateState { it.copy(errorMessage = null) }
        }
    }

    private fun observeLocalNotices(type: NoticeType) {
        observeJob?.cancel()
        observeJob = viewModelScope.launch {
            updateState { it.copy(noticeList = emptyList()) }
            noticeRepository.observeNotice(type).collect { notices ->
                //Log.d(TAG, "Notice type: ${type.typeId}")
                updateState { it.copy(noticeList = notices) }
            }
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
                            //updateState { it.copy(errorMessage = null) }
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
        _uiState.value = update(_uiState.value)
    }
}