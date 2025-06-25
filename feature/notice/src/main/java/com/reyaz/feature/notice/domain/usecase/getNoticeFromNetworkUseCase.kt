package com.reyaz.feature.notice.domain.usecase

import android.util.Log
import com.reyaz.core.common.utlis.NetworkManager
import com.reyaz.core.common.utlis.NetworkPreference
import com.reyaz.core.network.utils.RequestTimeStore
import com.reyaz.feature.notice.data.NoticeRepository
import com.reyaz.feature.notice.data.model.NoticeType
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.first

private const val TAG = "GET_NOTICE_USE_CASE"

class GetNoticeFromNetworkUseCase(
    private val requestTimeStore: RequestTimeStore,
    private val noticeRepository: NoticeRepository,
    private val networkManager: NetworkManager
) {
    suspend operator fun invoke(type: NoticeType, forceRefresh: Boolean = false, threshHoldMin: Int = 10) : Result<Unit> {
        //Log.d(TAG, "Should Refresh: ${requestTimeStore.shouldRefresh(type.typeId)}")
        return if (forceRefresh || requestTimeStore.shouldRefresh(typeId = type.typeId, threshHoldMin = threshHoldMin)) {
            Log.d(TAG, "Refreshing new data")
            if(networkManager.observeNetworkPreference().first() != NetworkPreference.NONE) {
                val result = noticeRepository.refreshNotice(type)
                if (result.isSuccess) {
                    requestTimeStore.saveRequestTime(type.typeId)
                }
                result
            } else {
                Result.failure(Exception("No internet connection"))
            }
        } else {
            Log.d(TAG, "Using cached data")
            Result.success(Unit)
        }
    }
}