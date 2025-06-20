package com.reyaz.feature.notice.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.reyaz.feature.notice.data.remote.NoticeScraper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

private const val TAG = "NOTICE_VIEW_MODEL"

class NoticeViewModel(
    private val noticeRepository: NoticeScraper
) : ViewModel() {
    private val _uiState = MutableStateFlow(NoticeUiState())
    val uiState = _uiState.asStateFlow()

    init {
        Log.d(TAG, "initialised")
        event(NoticeEvent.FetchNotice(""))
    }

    fun event(event: NoticeEvent) {
        when (event) {
            is NoticeEvent.FetchNotice -> fetchNotice(event.type)
            else -> {
                Log.d(TAG, "Unknown event: $event")
            }
        }
    }

    private fun fetchNotice(type: String) {
            viewModelScope.launch {
                noticeRepository.scrapNotices()
            }
    }
}