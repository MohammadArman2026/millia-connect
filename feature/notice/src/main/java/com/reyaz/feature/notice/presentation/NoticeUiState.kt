package com.reyaz.feature.notice.presentation

import com.reyaz.feature.notice.domain.model.Notice

data class NoticeUiState(
    val noticeList: List<Notice> = emptyList(),
    val isLoading: Boolean = false,
//    val isRefreshing: Boolean = false,
    val errorMessage: String? = null,
    val selectedTabIndex: Int = 0
){
    val unreadCount: Int = noticeList.count { !it.isRead }
}
