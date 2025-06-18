package com.reyaz.feature.result.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.reyaz.feature.result.domain.repository.ResultRepository


private const val TAG = "RESULT_WORKER_MANAGER"
class ResultSyncWorker(
    appContext: Context,
    workerParams: WorkerParameters,
    private val resultRepository: ResultRepository
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result {
        resultRepository.refreshLocalResults()
        Log.d(TAG, "Refreshing local results using work manager")
        return Result.success()
    }
}