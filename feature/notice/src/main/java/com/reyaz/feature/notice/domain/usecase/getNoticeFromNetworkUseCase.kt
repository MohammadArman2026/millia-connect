package com.reyaz.feature.notice.domain.usecase

import android.util.Log
import com.reyaz.core.network.utils.RequestTimeStore
import com.reyaz.feature.notice.data.NoticeRepository
import com.reyaz.feature.notice.data.model.NoticeType

private const val TAG = "GET_NOTICE_USE_CASE"

class GetNoticeFromNetworkUseCase(
    private val requestTimeStore: RequestTimeStore,
    private val noticeRepository: NoticeRepository
) {
    suspend operator fun invoke(type: NoticeType, forceRefresh: Boolean = false, threshHoldHours: Int = 8) : Result<Unit> {
        Log.d(TAG, "Should Refresh: ${requestTimeStore.shouldRefresh(type.typeId)}")
        return if (forceRefresh || requestTimeStore.shouldRefresh(typeId = type.typeId, threshHoldHours = threshHoldHours)) {
            Log.d(TAG, "Refreshing new data")
            val result = noticeRepository.refreshNotice(type)
            if (result.isSuccess) {
                requestTimeStore.saveRequestTime(type.typeId)
            }
            result
        } else {
            Log.d(TAG, "Using cached data")
            Result.success(Unit)
        }
    }
}