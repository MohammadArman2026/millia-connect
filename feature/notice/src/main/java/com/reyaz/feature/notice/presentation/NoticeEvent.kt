package com.reyaz.feature.notice.presentation

import com.reyaz.feature.notice.data.model.NoticeType

sealed class NoticeEvent{
    data class ObserveNotice (val type: NoticeType) : NoticeEvent()
    data class FetchLocalNotice (val type: NoticeType) : NoticeEvent()
    data class RefreshNotice (val type: NoticeType) : NoticeEvent()
    data class DownloadPdf (val url: String, val title: String) : NoticeEvent()
    data class DeleteFileByPath (val path: String, val title: String) : NoticeEvent()
    data class UpdateTabIndex (val index: Int) : NoticeEvent()

}
