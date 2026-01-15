package com.example.brewappsassignment.notifications

import androidx.work.*
import java.util.Calendar
import java.util.concurrent.TimeUnit

object NotificationScheduler {

    fun scheduleDailyQuote(hour: Int, minute: Int) {

        val now = Calendar.getInstance()
        val target = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
        }

        // If the time has already passed today, schedule for tomorrow
        if (target.before(now)) {
            target.add(Calendar.DAY_OF_YEAR, 1)
        }

        val delay = target.timeInMillis - now.timeInMillis

        val request = PeriodicWorkRequestBuilder<DailyQuoteWorker>(
            24, TimeUnit.HOURS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .build()

        WorkManager.getInstance().enqueueUniquePeriodicWork(
            "daily_quote_work",
            ExistingPeriodicWorkPolicy.UPDATE,
            request
        )
    }
}
