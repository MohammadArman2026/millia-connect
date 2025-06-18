package com.reyaz.feature.result.worker

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class WorkScheduler(
    private val context: Context
) {
    fun scheduleOneTime(
        initialDelay: Int = 0,
        timeUnit: TimeUnit = TimeUnit.HOURS,
        workName: String
    ) {
        val request = OneTimeWorkRequestBuilder<ResultSyncWorker>().build()
        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueWorkName = workName,
            existingWorkPolicy = ExistingWorkPolicy.REPLACE,
            request = request
        )
    }

    fun schedulePeriodic(
        repeatInterval: Int = 4,
        initialDelay: Int = repeatInterval,
        repeatTimeUnit: TimeUnit = TimeUnit.HOURS,
        flexInterval: Int = 1,
        flexTimeUnit: TimeUnit = TimeUnit.HOURS,
        workName: String
    ) {
        /**
         * flexTimeInterval defines a "flex window" that occurs at the end of your repeatInterval.
         * WorkManager will execute your periodic work somewhere within this flex window.
         *
         * Imagine a repeatInterval of 1 hour and a flexTimeInterval of 15 minutes:
         * |------------------ 45 min (no work) ------------------|--- 15 min (flex window) ---|
         * ^ Start of interval                                    ^ Start of flex window       ^ End of interval
         *                                                                  (Work could run anytime here)
         *
         * The flexTimeInterval must be less than or equal to repeatInterval - 5 minutes.
         * The minimum repeatInterval when using flexTimeInterval is 15 minutes.
         * The minimum flexTimeInterval is 5 minutes.
         * */
        val request = PeriodicWorkRequestBuilder<ResultSyncWorker>(
            repeatInterval = repeatInterval.toLong(),
            repeatIntervalTimeUnit = repeatTimeUnit,
            flexTimeInterval = flexInterval.toLong(),
            flexTimeIntervalUnit = flexTimeUnit
        )
            .setInitialDelay(initialDelay.toLong(), repeatTimeUnit)
            .build()
        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            uniqueWorkName = workName,
            existingPeriodicWorkPolicy = ExistingPeriodicWorkPolicy.CANCEL_AND_REENQUEUE,
            request = request
        )
    }

    fun cancelWork(tag: String) {
        WorkManager.getInstance(context).cancelUniqueWork(tag)
    }
}
