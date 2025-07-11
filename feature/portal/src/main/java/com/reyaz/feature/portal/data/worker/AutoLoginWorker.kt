package com.reyaz.feature.portal.data.worker

import android.content.Context
import android.util.Log
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.reyaz.core.common.utils.Resource
import com.reyaz.feature.portal.domain.repository.PortalRepository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class AutoLoginWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val portalRepository: PortalRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            var result: Result = Result.success()
            portalRepository.connect(shouldNotify = true).collect {
                when(it){
                    is Resource.Error -> {
                       result =  Result.retry()
                    }
                    is Resource.Success -> {
                        result = Result.success()
                    }
                    is Resource.Loading -> {}
                }
            }
            result
        } catch (e: Exception) {
            Log.e("AutoLoginWorker", "Error during auto login", e)
            Result.failure()
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "portal_login_work"

        fun scheduleOneTime(context: Context) {
            Log.d("AutoLoginWorker", "Scheduling auto login work")

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)  // wifi
                .build()

            val autoLoginTask = OneTimeWorkRequestBuilder<AutoLoginWorker>()
                .setConstraints(constraints)
                .setInitialDelay(100, TimeUnit.MINUTES)
                .setBackoffCriteria(
                    BackoffPolicy.EXPONENTIAL,
                    10,
                    TimeUnit.SECONDS
                )
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                uniqueWorkName = UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                autoLoginTask
            )
        }
    }
}
