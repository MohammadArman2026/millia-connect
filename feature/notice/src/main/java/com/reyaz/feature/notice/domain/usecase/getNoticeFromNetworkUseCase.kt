package com.reyaz.feature.notice.domain.usecase

import android.util.Log
import com.reyaz.core.common.Resource
import com.reyaz.core.common.utlis.NetworkManager
import com.reyaz.core.common.utlis.NetworkPreference
import com.reyaz.core.network.utils.RequestTimeStore
import com.reyaz.feature.notice.data.NoticeRepository
import com.reyaz.feature.notice.data.model.NoticeType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

private const val TAG = "GET_NOTICE_USE_CASE"

class GetNoticeFromNetworkUseCase(
    private val requestTimeStore: RequestTimeStore,
    private val noticeRepository: NoticeRepository,
    private val networkManager: NetworkManager
) {
    operator fun invoke(
        type: NoticeType,
        forceRefresh: Boolean = false,
    ): Flow<Resource<Unit>> = flow {
        if (forceRefresh || requestTimeStore.shouldRefresh(
                typeId = type.typeId,
            )
        ) {
            emit(Resource.Loading())
            Log.d(TAG, "Refreshing new data")
            if (networkManager.observeNetworkPreference().first() != NetworkPreference.NONE) {
                val result = noticeRepository.refreshNotice(type)
                if (result.isSuccess) {
                    requestTimeStore.saveRequestTime(type.typeId)
                }
                emit(Resource.Success(Unit))
            } else {
                emit(Resource.Error("No internet connection"))
            }
        } else {
            Log.d(TAG, "Using cached data")
            emit(Resource.Success())
        }
    }.flowOn(Dispatchers.IO)
}