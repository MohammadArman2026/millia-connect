package com.reyaz.milliaconnect.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.reyaz.milliaconnect.data.UserPreferences
import com.reyaz.milliaconnect.data.WebLoginManager
import com.reyaz.milliaconnect.util.NotificationHelper
import kotlinx.coroutines.flow.first
import java.time.Duration

import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.util.concurrent.TimeUnit

class AutoLoginWorker(
    context: Context,
    params: WorkerParameters
) : CoroutineWorker(context, params), KoinComponent {

    private val userPreferences: UserPreferences by inject()
    private val webLoginManager: WebLoginManager by inject()
    private val notificationHelper: NotificationHelper by inject()

    override suspend fun doWork(): Result {
        Log.d("AutoLoginWorker", "Starting auto login work")

        notificationHelper.showNotification("start do work", "doing schedule work")

        // Check if user is supposed to be logged in
        val isLoggedIn = userPreferences.loginStatus.first()
        if (!isLoggedIn) {
            Log.d("AutoLoginWorker", "User not logged in, skipping auto login")
            return Result.success()
        }

        // Get stored credentials
        val username = userPreferences.username.first()
        val password = userPreferences.password.first()

        return try {
            webLoginManager.performLogin(username, password)
                .fold(
                    onSuccess = {
                        Log.d("AutoLoginWorker", "Auto login successful")
//                        schedule(context = applicationContext)
                        //notificationHelper.showNotification("do work", "schedule work Successfully")
                        Result.success()
                    },
                    onFailure = {
                        Log.e("AutoLoginWorker", "Auto login failed", it)
                        Result.retry()
                    }
                )
        } catch (e: Exception) {
            Log.e("AutoLoginWorker", "Error during auto login", e)
            Result.retry()
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "product_upload_work"

        fun schedule(context: Context) {
            Log.d("AutoLoginWorker", "Scheduling auto login work")
            val autoLoginTask = PeriodicWorkRequestBuilder<AutoLoginWorker>(7205, TimeUnit.SECONDS)
                .build()

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                UNIQUE_WORK_NAME,
                ExistingPeriodicWorkPolicy.KEEP,
                autoLoginTask
            )
        }

        fun cancel(context: Context) {
            Log.d("AutoLoginWorker", "Cancelling auto login work")
            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME)
            WorkManager.getInstance(context).cancelAllWork()
            WorkManager.getInstance(context).pruneWork()


        }

    }
}