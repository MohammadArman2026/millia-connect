package com.reyaz.feature.notice.domain.usecase

import com.reyaz.core.network.utils.RequestTimeStore
import com.reyaz.feature.notice.data.NoticeRepository
import com.reyaz.feature.notice.data.model.NoticeType

class GetNoticeFromNetworkUseCase(
    private val requestTimeStore: RequestTimeStore,
    private val noticeRepository: NoticeRepository
) {
    suspend operator fun invoke(type: NoticeType, forceRefresh: Boolean = false, threshHoldHours: Int = 8) : Result<Unit> {
        return if (forceRefresh || requestTimeStore.shouldRefresh(
                typeId = type.typeId,
                threshHoldHours = threshHoldHours
            )
        ) {
            val result = noticeRepository.refreshNotice(type)
            if (result.isSuccess) {
                requestTimeStore.saveRequestTime(type.typeId)
            }
            result
        } else {
            Result.success(Unit)
        }
    }
}