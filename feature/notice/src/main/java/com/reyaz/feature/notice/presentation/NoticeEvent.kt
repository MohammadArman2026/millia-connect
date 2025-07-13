package com.reyaz.feature.notice.presentation

import com.reyaz.feature.notice.data.model.NoticeType
import com.reyaz.feature.notice.domain.model.TabConfig

sealed class NoticeEvent{
//    data class ObserveNotice (val type: NoticeType) : NoticeEvent()
    data class FetchLocalNotice (val type: NoticeType) : NoticeEvent()
    data class FetchRemoteNotice (val type: NoticeType, val forceRefresh: Boolean) : NoticeEvent()
    data class DownloadPdf (val url: String, val title: String) : NoticeEvent()
    data class DeleteFileByPath (val path: String, val title: String) : NoticeEvent()
    data class OnTabClick (val tab: TabConfig) : NoticeEvent()

}
