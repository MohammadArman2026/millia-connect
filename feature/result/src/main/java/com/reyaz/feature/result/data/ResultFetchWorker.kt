package com.reyaz.feature.result.data

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.reyaz.feature.result.domain.repository.ResultRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

private const val TAG = "RESULT_FETCH_WORKER"

class ResultFetchWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams), KoinComponent {

    private val resultRepository: ResultRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            resultRepository.refreshLocalResults(shouldNotify = true)
            Log.d(TAG, "Result fetched successfully")
            Result.success()
        } catch (e: Exception) {
            if (runAttemptCount < 3) {
                Result.retry()
            } else {
                Result.failure()
            }
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "result_fetch_work"

        fun schedule(context: Context) {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val fetchResultWorkRequest = OneTimeWorkRequestBuilder<ResultFetchWorker>()
                .setConstraints(constraints)
                .setInitialDelay(24, TimeUnit.HOURS)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    10,
                    TimeUnit.MILLISECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                fetchResultWorkRequest
            )
        }
    }
}
