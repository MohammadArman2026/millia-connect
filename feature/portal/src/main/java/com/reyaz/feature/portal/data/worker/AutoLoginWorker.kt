package com.reyaz.feature.portal.data.worker

import android.content.Context
import android.util.Log
import android.widget.Toast
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
private const val TAG = "AUTO_LOGIN_WORKER"
class AutoLoginWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val portalRepository: PortalRepository by inject()

    override suspend fun doWork(): Result {
        return try {
            Log.d(TAG, "Performing auto login by worker")
            var result: Result = Result.success()
            portalRepository.connect(shouldNotify = true).collect {
                when(it){
                    is Resource.Error -> {
//                        result = if(runAttemptCount<3)
//                            Result.retry()
//                        else Result.failure()
                        result = Result.failure()
                    }
                    is Resource.Success -> {
                        result = Result.success()
                    }
                    is Resource.Loading -> {}
                }
            }
            Log.d(TAG, "Auto login result: $result")
            result
        } catch (e: Exception) {
            Log.e(TAG, "Error during auto login", e)
            Result.failure()
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "portal_login_work"

        fun scheduleOneTime(context: Context) {
            Log.d(TAG, "Scheduling auto login work")

            val constraints = Constraints.Builder()
                .build()

            val autoLoginTask = OneTimeWorkRequestBuilder<AutoLoginWorker>()
                .setConstraints(constraints)
                .setInitialDelay(100, TimeUnit.MINUTES)
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                uniqueWorkName = UNIQUE_WORK_NAME,
                ExistingWorkPolicy.REPLACE,
                autoLoginTask
            )
        }

        fun cancelOneTime(context: Context) {
            Log.d(TAG, "Cancelling auto login work")
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME)
        }
    }
}
