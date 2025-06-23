package com.reyaz.feature.notice.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.notice.data.NoticeRepository
import com.reyaz.feature.notice.data.model.NoticeType
import com.reyaz.feature.notice.domain.model.Tabs
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "NOTICE_VIEW_MODEL"

class NoticeViewModel(
    private val noticeRepository: NoticeRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        event(NoticeEvent.ObserveNotice(Tabs.entries[0].type))
    }

    fun event(event: NoticeEvent) {
        when (event) {
            is NoticeEvent.ObserveNotice -> {
                observeLocalNotices(event.type)
                refreshRemoteNotice(event.type)
            }
            is NoticeEvent.FetchLocalNotice -> observeLocalNotices(event.type)
            is NoticeEvent.RefreshNotice -> refreshRemoteNotice(event.type)
            else -> {
                Log.d(TAG, "Unknown event: $event")
            }
        }
    }

    private fun refreshRemoteNotice(type: NoticeType) {
        viewModelScope.launch {
            updateState { it.copy(isLoading = true) }
            val refreshResult = noticeRepository.refreshNotice(type)
            if (refreshResult.isSuccess) {
                updateState { it.copy(isLoading = false) }
            }
        }
    }

    private fun observeLocalNotices(type: NoticeType) {
        viewModelScope.launch {
            updateState { it.copy(noticeList = emptyList()) }
            noticeRepository.observeNotice(type).collect { notices ->
                Log.d(TAG, "Notices: ${notices}")
                updateState { it.copy(noticeList = notices) }
            }
        }
    }

    private fun updateState(update: (NoticeUiState) -> NoticeUiState) {
        _uiState.value = update(_uiState.value)
    }
}