package com.example.brewappsassignment.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

object NotificationHelper {

    const val CHANNEL_ID = "daily_quote_channel"

    fun createChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            val channel = NotificationChannel(
                CHANNEL_ID,
                "Daily Quotes",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Shows one inspirational quote every day"
            }

            val manager = context.getSystemService(NotificationManager::class.java)
            manager.createNotificationChannel(channel)
        }
    }
}
