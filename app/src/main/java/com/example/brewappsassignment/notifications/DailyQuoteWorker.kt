package com.example.brewappsassignment.notifications

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.brewappsassignment.R
import kotlin.random.Random

class DailyQuoteWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {

        NotificationHelper.createChannel(applicationContext)

        // Hydrate with a random quote
        val quotes = listOf(
            "Your only limit is you.",
            "Dream big and dare to fail.",
            "Believe you can and you're halfway there.",
            "Action is the foundational key to all success."
        )

        val randomQuote = quotes.random()

        val notification = NotificationCompat.Builder(applicationContext, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle("Daily Quote")
            .setContentText(randomQuote)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(applicationContext).notify(
            Random.nextInt(),
            notification
        )

        return Result.success()
    }
}
