package com.reyaz.feature.notice.presentation

sealed class NoticeEvent{
    data class FetchNotice (val type: String) : NoticeEvent()
}
