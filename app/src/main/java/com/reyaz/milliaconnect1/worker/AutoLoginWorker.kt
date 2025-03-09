package com.reyaz.milliaconnect1.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.reyaz.milliaconnect1.data.UserPreferences
import com.reyaz.milliaconnect1.data.WebLoginManager
import com.reyaz.milliaconnect1.util.NotificationHelper
import kotlinx.coroutines.flow.first
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
                        userPreferences.setLoginStatus(true)
                        notificationHelper.showNotification(
                            "Session Restored",
                            "Your Jamia Wifi session has been restored automatically"
                        )
//                        schedule(context = applicationContext)  //
                        Result.success()
                    },
                    onFailure = {
                        Log.e("AutoLoginWorker", "Auto login failed", it)
                        userPreferences.setLoginStatus(false)
                        notificationHelper.showNotification(
                            "Auto Login Failed",
                            "You were not connected to Jamia Wifi."
                        )
                        cancel(applicationContext)
                        Result.failure()
                    }
                )
        } catch (e: Exception) {
            Log.e("AutoLoginWorker", "Error during auto login", e)
            userPreferences.setLoginStatus(false)
            cancel(applicationContext)
            Result.failure()
        }
    }

    companion object {
        private const val UNIQUE_WORK_NAME = "product_upload_work"

        /**
         * Schedules a periodic background task for auto-login.
         *
         * This function uses WorkManager to schedule a worker ([AutoLoginWorker]) that will
         * periodically attempt to perform an auto-login.
         *
         * The worker is scheduled to run every 100 minutes. If a task with the same
         * unique name is already scheduled, the existing task will be kept and the new
         * one will be ignored (due to [ExistingPeriodicWorkPolicy.KEEP]).
         *
         * The unique name for the task is based on the current system time (in milliseconds),
         * ensuring each scheduled task has a distinct name. This is acceptable because the KEEP
         * policy will ensure it only runs once at a given time interval.
         *
         * @param context The application context. This is needed to access WorkManager.
         *
         * @see AutoLoginWorker
         * @see WorkManager
         * @see PeriodicWorkRequestBuilder
         * @see ExistingPeriodicWorkPolicy
         */

        //periodic work
        fun schedule(context: Context) {
            Log.d("AutoLoginWorker", "scheduling auto login work")
            AutoLoginWorker.cancel(context)
            val autoLoginTask = PeriodicWorkRequestBuilder<AutoLoginWorker>(100, TimeUnit.MINUTES)
                .setInitialDelay(100, TimeUnit.MINUTES)
                .build()
            val id = autoLoginTask.id
            Log.d("AutoLoginWorker", "id: $id")

            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
                uniqueWorkName =
                UNIQUE_WORK_NAME,
//                System.currentTimeMillis().toString(),
                existingPeriodicWorkPolicy =
//                ExistingPeriodicWorkPolicy.UPDATE,
                ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
//                ExistingPeriodicWorkPolicy.KEEP,
                request = autoLoginTask
            )
        }

        // one time work
        fun schedule1(context: Context) {
            Log.d("AutoLoginWorker", "Scheduling auto login work")
//            val autoLoginTask = PeriodicWorkRequestBuilder<AutoLoginWorker>(100, TimeUnit.MINUTES)
          val autoLoginTask = OneTimeWorkRequestBuilder<AutoLoginWorker>()
                .setInitialDelay(5, TimeUnit.SECONDS)
                .build()

//            WorkManager.getInstance(context).enqueueUniquePeriodicWork(
          WorkManager.getInstance(context).enqueueUniqueWork(
                uniqueWorkName = UNIQUE_WORK_NAME,
//                System.currentTimeMillis().toString(),
              ExistingWorkPolicy.APPEND_OR_REPLACE,
                autoLoginTask
            )
        }

        fun cancel(context: Context) {
            Log.d("AutoLoginWorker", "Cancelling auto login work")
//            WorkManager.getInstance(context).cancelUniqueWork(UNIQUE_WORK_NAME)
            WorkManager.getInstance(context).cancelAllWork()
//            WorkManager.getInstance(context).pruneWork()
        }
    }
}