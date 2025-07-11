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
            Log.d(TAG, "Starting work for Fetching result")
            resultRepository.refreshLocalResults(shouldNotify = true)
            Log.d(TAG, "Work completed for Fetching result")
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
            Log.d(TAG, "Scheduling result fetch work")
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val fetchResultWorkRequest = OneTimeWorkRequestBuilder<ResultFetchWorker>()
                .setConstraints(constraints)
                .setInitialDelay(30, TimeUnit.MINUTES)
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
